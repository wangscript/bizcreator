package org.apache.ibatis.builder.annotation;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.JdbcType;

import java.io.IOException;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class MapperAnnotationBuilder {

  private Configuration configuration;
  private MapperBuilderAssistant assistant;
  private Class type;

  public MapperAnnotationBuilder(Configuration configuration, Class type) {
    String resource = type.getName().replace('.', '/') + ".java (best guess)";
    this.assistant = new MapperBuilderAssistant(configuration, resource);
    this.configuration = configuration;
    this.type = type;
  }

  public void parse() {
    String resource = type.toString();
    if (!configuration.isResourceLoaded(resource)) {
      configuration.addLoadedResource(resource);
      loadXmlResource();
      assistant.setCurrentNamespace(type.getName());
      parseCache();
      parseCacheRef();
      Method[] methods = type.getMethods();
      for (Method method : methods) {
        parseResultsAndConstructorArgs(method);
        parseStatement(method);
      }
    }
  }

  private void loadXmlResource() {
    String xmlResource = type.getName().replace('.', '/') + ".xml";
    Reader xmlReader = null;
    try {
      xmlReader = Resources.getResourceAsReader(type.getClassLoader(), xmlResource);
    } catch (IOException e) {
      // ignore, resource is not required
    }
    if (xmlReader != null) {
      XMLMapperBuilder xmlParser = new XMLMapperBuilder(xmlReader, assistant.getConfiguration(), xmlResource, new HashMap(), type.getName());
      xmlParser.parse();
    }
  }

  private void parseCache() {
    CacheNamespace cacheDomain = (CacheNamespace) type.getAnnotation(CacheNamespace.class);
    if (cacheDomain != null) {
      assistant.useNewCache(cacheDomain.implementation(), cacheDomain.eviction(), cacheDomain.flushInterval(), cacheDomain.size(), !cacheDomain.readWrite(), null);
    }
  }

  private void parseCacheRef() {
    CacheNamespaceRef cacheDomainRef = (CacheNamespaceRef) type.getAnnotation(CacheNamespaceRef.class);
    if (cacheDomainRef != null) {
      assistant.useCacheRef(cacheDomainRef.value().getName());
    }
  }

  private void parseResultsAndConstructorArgs(Method method) {
    Class returnType = getReturnType(method);
    if (returnType != null) {
      ConstructorArgs args = method.getAnnotation(ConstructorArgs.class);
      Results results = method.getAnnotation(Results.class);
      TypeDiscriminator typeDiscriminator = method.getAnnotation(TypeDiscriminator.class);
      String resultMapId = generateResultMapName(method);
      applyResultMap(resultMapId, returnType, argsIf(args), resultsIf(results), typeDiscriminator);
    }
  }

  private String generateResultMapName(Method method) {
    StringBuilder suffix = new StringBuilder();
    for (Class c : method.getParameterTypes()) {
      suffix.append("-");
      suffix.append(c.getSimpleName());
    }
    if (suffix.length() < 1) {
      suffix.append("-void");
    }
    return type.getName() + "." + method.getName() + suffix;
  }

  private void applyResultMap(String resultMapId, Class returnType, Arg[] args, Result[] results, TypeDiscriminator discriminator) {
    List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
    applyConstructorArgs(args, returnType, resultMappings);
    applyResults(results, returnType, resultMappings);
    Discriminator disc = applyDiscriminator(resultMapId, returnType, discriminator);
    assistant.addResultMap(resultMapId, returnType, null, disc, resultMappings);
    createDiscriminatorResultMaps(resultMapId, returnType, discriminator);
  }

  private void createDiscriminatorResultMaps(String resultMapId, Class resultType, TypeDiscriminator discriminator) {
    if (discriminator != null) {
      for (Case c : discriminator.cases()) {
        String value = c.value();
        Class type = c.type();
        String caseResultMapId = resultMapId + "-" + value;
        List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
        for (Result result : c.results()) {
          List<ResultFlag> flags = new ArrayList<ResultFlag>();
          if (result.id()) {
            flags.add(ResultFlag.ID);
          }
          ResultMapping resultMapping = assistant.buildResultMapping(
              resultType,
              result.property(),
              result.column(),
              result.javaType() == void.class ? null : result.javaType(),
              result.jdbcType() == JdbcType.UNDEFINED ? null : result.jdbcType(),
              hasNestedSelect(result) ? nestedSelectId(result) : null,
              null,
              result.typeHandler() == void.class ? null : result.typeHandler(),
              flags);
          resultMappings.add(resultMapping);
        }
        assistant.addResultMap(caseResultMapId, type, resultMapId, null, resultMappings);
      }
    }
  }

  private Discriminator applyDiscriminator(String resultMapId, Class resultType, TypeDiscriminator discriminator) {
    if (discriminator != null) {
      String column = discriminator.column();
      Class javaType = discriminator.javaType() == void.class ? String.class : discriminator.javaType();
      JdbcType jdbcType = discriminator.jdbcType() == JdbcType.UNDEFINED ? null : discriminator.jdbcType();
      Class typeHandler = discriminator.typeHandler() == void.class ? null : discriminator.typeHandler();
      Case[] cases = discriminator.cases();
      Map<String, String> discriminatorMap = new HashMap<String, String>();
      for (Case c : cases) {
        String value = c.value();
        String caseResultMapId = resultMapId + "-" + value;
        discriminatorMap.put(value, caseResultMapId);
      }
      return assistant.buildDiscriminator(resultType, column, javaType, jdbcType, typeHandler, discriminatorMap);
    }
    return null;
  }

  private void parseStatement(Method method) {
    Configuration configuration = assistant.getConfiguration();
    SqlSource sqlSource = getSqlSourceFromAnnotations(method);
    if (sqlSource != null) {
      Options options = method.getAnnotation(Options.class);
      final String mappedStatementId = method.getDeclaringClass().getName() + "." + method.getName();
      boolean flushCache = false;
      boolean useCache = true;
      Integer fetchSize = null;
      Integer timeout = null;
      StatementType statementType = StatementType.PREPARED;
      ResultSetType resultSetType = ResultSetType.FORWARD_ONLY;
      SqlCommandType sqlCommandType = getSqlCommandType(method);
      KeyGenerator keyGenerator = configuration.isUseGeneratedKeys()
          && SqlCommandType.INSERT.equals(sqlCommandType) ? new Jdbc3KeyGenerator() : new NoKeyGenerator();
      String keyProperty = "id";
      if (options != null) {
        flushCache = options.flushCache();
        useCache = options.useCache();
        fetchSize = options.fetchSize() > -1 ? options.fetchSize() : null;
        timeout = options.timeout() > -1 ? options.timeout() : null;
        statementType = options.statementType();
        resultSetType = options.resultSetType();
        keyGenerator = options.useGeneratedKeys() ? new Jdbc3KeyGenerator() : null;
        keyProperty = options.keyProperty();
      }
      assistant.addMappedStatement(
          mappedStatementId,
          sqlSource,
          statementType,
          sqlCommandType,
          fetchSize,
          timeout,
          null,                             // ParameterMapID
          getParameterType(method),
          generateResultMapName(method),    // ResultMapID
          getReturnType(method),
          resultSetType,
          flushCache,
          useCache,
          keyGenerator,
          keyProperty);
    }
  }

  private Class getParameterType(Method method) {
    Class parameterType = null;
    Class[] parameterTypes = method.getParameterTypes();
    for(int i=0; i<parameterTypes.length;i++) {
      if (!RowBounds.class.isAssignableFrom(parameterTypes[i])) {
        if (parameterType == null) {
          parameterType = parameterTypes[i];
        } else {
          parameterType = Map.class;
        }
      }
    }
    return parameterType;
  }

  private Class getReturnType(Method method) {
    Class returnType = method.getReturnType();
    if (Collection.class.isAssignableFrom(returnType)) {
      Type returnTypeParameter = method.getGenericReturnType();
      if (returnTypeParameter instanceof ParameterizedType) {
        Type[] actualTypeArguments = ((ParameterizedType) returnTypeParameter).getActualTypeArguments();
        if (actualTypeArguments != null && actualTypeArguments.length == 1) {
          returnTypeParameter = actualTypeArguments[0];
          if (returnTypeParameter instanceof Class) {
            returnType = (Class) returnTypeParameter;
          }
        }
      }
    }
    return returnType;
  }

  private SqlSource getSqlSourceFromAnnotations(Method method) {
    try {
      Class sqlAnnotationType = getSqlAnnotationType(method);
      Class sqlProviderAnnotationType = getSqlProviderAnnotationType(method);
      if (sqlAnnotationType != null) {
        if (sqlProviderAnnotationType != null) {
          throw new BindingException("You cannot supply both a static SQL and SqlProvider to method named " + method.getName());
        }
        Annotation sqlAnnotation = method.getAnnotation(sqlAnnotationType);
        final String[] strings = (String[]) sqlAnnotation.getClass().getMethod("value").invoke(sqlAnnotation);
        StringBuilder sql = new StringBuilder();
        for (String fragment : strings) {
          sql.append(fragment);
          sql.append(" ");
        }
        SqlSourceBuilder parser = new SqlSourceBuilder(assistant.getConfiguration());
        return parser.parse(sql.toString(), getParameterType(method));
      } else if (sqlProviderAnnotationType != null) {
        Annotation sqlProviderAnnotation = method.getAnnotation(sqlProviderAnnotationType);
        return new ProviderSqlSource(assistant.getConfiguration(), sqlProviderAnnotation);
      }
      return null;
    } catch (Exception e) {
      throw new BuilderException("Could not find value method on SQL annotation.  Cause: " + e, e);
    }
  }

  private SqlCommandType getSqlCommandType(Method method) {
    Class[] types = {Select.class, Insert.class, Update.class, Delete.class,
        SelectProvider.class, InsertProvider.class, UpdateProvider.class, DeleteProvider.class};
    Class type = chooseAnnotationType(method, types);
    if (type != null) {
      if (type == SelectProvider.class) {
        type = Select.class;
      } else if (type == InsertProvider.class) {
        type = Insert.class;
      } else if (type == UpdateProvider.class) {
        type = Update.class;
      } else if (type == DeleteProvider.class) {
        type = Delete.class;
      }
      return SqlCommandType.valueOf(type.getSimpleName().toUpperCase());
    }
    return SqlCommandType.UNKNOWN;
  }

  private Class getSqlAnnotationType(Method method) {
    Class[] types = {Select.class, Insert.class, Update.class, Delete.class};
    return chooseAnnotationType(method, types);
  }

  private Class getSqlProviderAnnotationType(Method method) {
    Class[] types = {SelectProvider.class, InsertProvider.class, UpdateProvider.class, DeleteProvider.class};
    return chooseAnnotationType(method, types);
  }

  private Class chooseAnnotationType(Method method, Class[] types) {
    for (Class type : types) {
      Annotation annotation = method.getAnnotation(type);
      if (annotation != null) {
        return type;
      }
    }
    return null;
  }

  private void applyResults(Result[] results, Class resultType, List<ResultMapping> resultMappings) {
    if (results.length > 0) {
      for (Result result : results) {
        ArrayList<ResultFlag> flags = new ArrayList<ResultFlag>();
        if (result.id()) flags.add(ResultFlag.ID);

        ResultMapping resultMapping = assistant.buildResultMapping(
            resultType,
            result.property(),
            result.column(),
            result.javaType() == void.class ? null : result.javaType(),
            result.jdbcType() == JdbcType.UNDEFINED ? null : result.jdbcType(),
            hasNestedSelect(result) ? nestedSelectId(result) : null,
            null,
            result.typeHandler() == void.class ? null : result.typeHandler(),
            flags);
        resultMappings.add(resultMapping);
      }
    }
  }

  private String nestedSelectId(Result result) {
    String nestedSelect = result.one().select();
    if (nestedSelect.length() < 1) {
      nestedSelect = result.many().select();
    }
    if (!nestedSelect.contains(".")) {
      nestedSelect = type.getName() + "." + nestedSelect;
    }
    return nestedSelect;
  }

  private boolean hasNestedSelect(Result result) {
    return result.one().select().length() > 0
        || result.many().select().length() > 0;
  }

  private void applyConstructorArgs(Arg[] args, Class resultType, List<ResultMapping> resultMappings) {
    if (args.length > 0) {
      for (Arg arg : args) {
        ArrayList<ResultFlag> flags = new ArrayList<ResultFlag>();
        flags.add(ResultFlag.CONSTRUCTOR);
        if (arg.id()) flags.add(ResultFlag.ID);
        ResultMapping resultMapping = assistant.buildResultMapping(
            resultType,
            null,
            arg.column(),
            arg.javaType() == void.class ? null : arg.javaType(),
            arg.jdbcType() == JdbcType.UNDEFINED ? null : arg.jdbcType(),
            null,
            null,
            arg.typeHandler() == void.class ? null : arg.typeHandler(),
            flags);
        resultMappings.add(resultMapping);
      }
    }
  }

  private Result[] resultsIf(Results results) {
    return results == null ? new Result[0] : results.value();
  }

  private Arg[] argsIf(ConstructorArgs args) {
    return args == null ? new Arg[0] : args.value();
  }

}
