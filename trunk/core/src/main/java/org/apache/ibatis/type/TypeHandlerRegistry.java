package org.apache.ibatis.type;

import java.math.BigDecimal;
import java.util.*;

public class TypeHandlerRegistry {

  private static final Map<Class, Class> reversePrimitiveMap = new HashMap<Class, Class>() {
    {
      put(Byte.class, byte.class);
      put(Short.class, short.class);
      put(Integer.class, int.class);
      put(Long.class, long.class);
      put(Float.class, float.class);
      put(Double.class, double.class);
      put(Boolean.class, boolean.class);
    }
  };

  private final Map<Class, Map<JdbcType, TypeHandler>> TYPE_HANDLER_MAP = new HashMap<Class, Map<JdbcType, TypeHandler>>();
  private final TypeHandler UNKNOWN_TYPE_HANDLER = new UnknownTypeHandler(this);

  public TypeHandlerRegistry() {
    register(Boolean.class, new BooleanTypeHandler());
    register(boolean.class, new BooleanTypeHandler());

    register(Byte.class, new ByteTypeHandler());
    register(byte.class, new ByteTypeHandler());

    register(Short.class, new ShortTypeHandler());
    register(short.class, new ShortTypeHandler());

    register(Integer.class, new IntegerTypeHandler());
    register(int.class, new IntegerTypeHandler());

    register(Long.class, new LongTypeHandler());
    register(long.class, new LongTypeHandler());

    register(Float.class, new FloatTypeHandler());
    register(float.class, new FloatTypeHandler());

    register(Double.class, new DoubleTypeHandler());
    register(double.class, new DoubleTypeHandler());

    register(String.class, new StringTypeHandler());
    register(String.class, JdbcType.CHAR, new StringTypeHandler());
    register(String.class, JdbcType.CLOB, new ClobTypeHandler());
    register(String.class, JdbcType.LONGVARCHAR, new ClobTypeHandler());
    register(String.class, JdbcType.NVARCHAR, new NStringTypeHandler());
    register(String.class, JdbcType.NCHAR, new NStringTypeHandler());
    register(String.class, JdbcType.NCLOB, new NClobTypeHandler());

    register(BigDecimal.class, new BigDecimalTypeHandler());

    register(byte[].class, new ByteArrayTypeHandler());
    register(byte[].class, JdbcType.BLOB, new BlobTypeHandler());
    register(byte[].class, JdbcType.LONGVARBINARY, new BlobTypeHandler());

    register(Object.class, new ObjectTypeHandler());
    register(Object.class, JdbcType.OTHER, new ObjectTypeHandler());

    register(Date.class, new DateTypeHandler());
    register(Date.class, JdbcType.DATE, new DateOnlyTypeHandler());
    register(Date.class, JdbcType.TIME, new TimeOnlyTypeHandler());

    register(java.sql.Date.class, new SqlDateTypeHandler());
    register(java.sql.Time.class, new SqlTimeTypeHandler());
    register(java.sql.Timestamp.class, new SqlTimestampTypeHandler());
  }

  public boolean hasTypeHandler(Class javaType) {
    return hasTypeHandler(javaType, null);
  }

  public boolean hasTypeHandler(Class javaType, JdbcType jdbcType) {
    return javaType != null && getTypeHandler(javaType, jdbcType) != null;
  }

  public TypeHandler getTypeHandler(Class type) {
    return getTypeHandler(type, null);
  }

  public TypeHandler getTypeHandler(Class type, JdbcType jdbcType) {
    Map jdbcHandlerMap = TYPE_HANDLER_MAP.get(type);
    TypeHandler handler = null;
    if (jdbcHandlerMap != null) {
      handler = (TypeHandler) jdbcHandlerMap.get(jdbcType);
      if (handler == null) {
        handler = (TypeHandler) jdbcHandlerMap.get(null);
      }
    }
    if (handler == null && type != null && Enum.class.isAssignableFrom(type)) {
      handler = new EnumTypeHandler(type);
    }
    return handler;
  }

  public TypeHandler getUnkownTypeHandler() {
    return UNKNOWN_TYPE_HANDLER;
  }

  public void register(Class type, TypeHandler handler) {
    register(type, null, handler);
  }

  public void register(Class type, JdbcType jdbcType, TypeHandler handler) {
    Map<JdbcType, TypeHandler> map = TYPE_HANDLER_MAP.get(type);
    if (map == null) {
      map = new HashMap<JdbcType, TypeHandler>();
      TYPE_HANDLER_MAP.put(type, map);
    }
    map.put(jdbcType, handler);
    if (reversePrimitiveMap.containsKey(type)) {
      register(reversePrimitiveMap.get(type), jdbcType, handler);
    }
  }

}
