package org.apache.ibatis.builder;

import org.apache.ibatis.mapping.*;
import org.apache.ibatis.type.*;

public abstract class BaseBuilder {
  protected final Configuration configuration;
  protected final TypeAliasRegistry typeAliasRegistry;
  protected final TypeHandlerRegistry typeHandlerRegistry;

  public BaseBuilder(Configuration configuration) {
    this.configuration = configuration;
    this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
    this.typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
  }

  public Configuration getConfiguration() {
    return configuration;
  }

  protected String stringValueOf(String value, String defaultValue) {
    return value == null ? defaultValue : value;
  }

  protected Boolean booleanValueOf(String value, Boolean defaultValue) {
    return value == null ? defaultValue : Boolean.valueOf(value);
  }

  protected Integer integerValueOf(String value, Integer defaultValue) {
    return value == null ? defaultValue : Integer.valueOf(value);
  }

  protected JdbcType resolveJdbcType(String alias) {
    if (alias == null) return null;
    try {
      return JdbcType.valueOf(alias);
    } catch (IllegalArgumentException e) {
      throw new BuilderException("Error resolving JdbcType. Cause: " + e, e);
    }
  }

  protected ResultSetType resolveResultSetType(String alias) {
    if (alias == null) return null;
    try {
      return ResultSetType.valueOf(resolveAlias(alias));
    } catch (IllegalArgumentException e) {
      throw new BuilderException("Error resolving ResultSetType. Cause: " + e, e);
    }
  }

  protected ParameterMode resolveParameterMode(String alias) {
    if (alias == null) return null;
    try {
      return ParameterMode.valueOf(resolveAlias(alias));
    } catch (IllegalArgumentException e) {
      throw new BuilderException("Error resolving ParameterMode. Cause: " + e, e);
    }
  }

  protected Class resolveClass(String alias) {
    if (alias == null) return null;
    try {
      return Class.forName(resolveAlias(alias));
    } catch (ClassNotFoundException e) {
      throw new BuilderException("Error resolving class . Cause: " + e, e);
    }
  }

  protected Object resolveInstance(String alias) {
    if (alias == null) return null;
    try {
      Class type = resolveClass(alias);
      return type.newInstance();
    } catch (Exception e) {
      throw new BuilderException("Error instantiating class. Cause: " + e, e);
    }
  }

  protected Object resolveInstance(Class type) {
    if (type == null) return null;
    try {
      return type.newInstance();
    } catch (Exception e) {
      throw new BuilderException("Error instantiating class. Cause: " + e, e);
    }
  }

  protected String resolveAlias(String alias) {
    return typeAliasRegistry.resolveAlias(alias);
  }
}
