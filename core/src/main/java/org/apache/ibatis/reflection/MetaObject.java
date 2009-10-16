package org.apache.ibatis.reflection;

import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.BeanWrapper;
import org.apache.ibatis.reflection.wrapper.MapWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;

import java.util.Map;

public class MetaObject {

  private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
  public static final MetaObject NULL_META_OBJECT = new MetaObject(NullObject.class, DEFAULT_OBJECT_FACTORY);

  private Object originalObject;
  private ObjectWrapper objectWrapper;
  private ObjectFactory objectFactory;

  private MetaObject(Object object, ObjectFactory objectFactory) {
    this.originalObject = object;
    this.objectFactory = objectFactory;
    if (object instanceof ObjectWrapper) {
      this.objectWrapper = (ObjectWrapper) object;
    } else if (object instanceof Map) {
      this.objectWrapper = new MapWrapper(this, (Map) object);
    } else {
      this.objectWrapper = new BeanWrapper(this, object);
    }
  }

  public static MetaObject forObject(Object object, ObjectFactory objectFactory) {
    if (object == null) {
      return NULL_META_OBJECT;
    } else {
      return new MetaObject(object, objectFactory);
    }
  }

  public static MetaObject forObject(Object object) {
    return forObject(object, DEFAULT_OBJECT_FACTORY);
  }

  public Object getOriginalObject() {
    return originalObject;
  }

  public String findProperty(String propName) {
    return objectWrapper.findProperty(propName);
  }

  public String[] getGetterNames() {
    return objectWrapper.getGetterNames();
  }

  public String[] getSetterNames() {
    return objectWrapper.getSetterNames();
  }

  public Class getSetterType(String name) {
    return objectWrapper.getSetterType(name);
  }

  public Class getGetterType(String name) {
    return objectWrapper.getGetterType(name);
  }

  public boolean hasSetter(String name) {
    return objectWrapper.hasSetter(name);
  }

  public boolean hasGetter(String name) {
    return objectWrapper.hasGetter(name);
  }

  public Object getValue(String name) {
    PropertyTokenizer prop = new PropertyTokenizer(name);
    if (prop.hasNext()) {
      MetaObject metaValue = metaObjectForProperty(prop.getIndexedName());
      if (metaValue == MetaObject.NULL_META_OBJECT) {
        return null;
      } else {
        return metaValue.getValue(prop.getChildren());
      }
    } else {
      return objectWrapper.get(prop);
    }
  }

  public void setValue(String name, Object value) {
    PropertyTokenizer prop = new PropertyTokenizer(name);
    if (prop.hasNext()) {
      MetaObject metaValue = metaObjectForProperty(prop.getIndexedName());
      if (metaValue == MetaObject.NULL_META_OBJECT) {
        if (value == null && prop.getChildren() != null) {
          return; // don't instantiate child path if value is null
        } else {
          metaValue = objectWrapper.instantiatePropertyValue(name, prop, objectFactory);
        }
      }
      metaValue.setValue(prop.getChildren(), value);
    } else {
      objectWrapper.set(prop, value);
    }
  }

  public MetaObject metaObjectForProperty(String name) {
    Object value = getValue(name);
    return MetaObject.forObject(value);
  }

  public ObjectWrapper getObjectWrapper() {
    return objectWrapper;
  }

  private static class NullObject {
  }

}
