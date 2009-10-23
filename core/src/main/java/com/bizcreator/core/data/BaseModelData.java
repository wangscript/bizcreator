/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Default <code>ModelData</code> implementation.
 */
public class BaseModelData implements ModelData, Serializable {

  protected Map map;
  protected boolean allowNestedValues = true;

  /**
   * Creates a new model data instance.
   */
  public BaseModelData() {
  }

  /**
   * Creates a new model with the given properties.
   *
   * @param properties the initial properties
   */
  public BaseModelData(Map<String, Object> properties) {
    super();
    setProperties(properties);
  }

  @SuppressWarnings("unchecked")
  public <X> X get(String property) {
    if (allowNestedValues && NestedModelUtil.isNestedProperty(property)) {
      return (X) NestedModelUtil.getNestedValue(this, property);
    }
    return map == null ? null : (X) map.get(property);
  }

  /**
   * Returns a property value.
   *
   * @param property the property name
   * @param valueWhenNull
   * @return the value
   */
  @SuppressWarnings("unchecked")
  public <X> X get(String property, X valueWhenNull) {
    X value = (X) get(property);
    return (value == null) ? valueWhenNull : value;
  }

  public Map<String, Object> getProperties() {
    HashMap<String, Object> newMap = new HashMap<String, Object>();
    if (map != null) {
      //newMap.putAll(map.getTransientMap());
        newMap.putAll(map);
    }
    return newMap;
  }

  public Collection<String> getPropertyNames() {
    Set<String> set = new HashSet<String>();
    if (map != null) {
      set.addAll(map.keySet());
    }
    return set;
  }

  /**
   * Returns true if nested values are enabled.
   *
   * @return the nested values state
   */
  public boolean isAllowNestedValues() {
    return allowNestedValues;
  }

  @SuppressWarnings("unchecked")
  public <X> X remove(String property) {
    return map == null ? null : (X) map.remove(property);
  }

  /**
   * Sets the property and fires an <i>Update</i> event.
   *
   * @param property the property name
   * @param value the property value
   */
  @SuppressWarnings("unchecked")
  public <X> X set(String property, X value) {
    if (map == null) {
      //map = new RpcMap();
        map = new HashMap();
    }
    if (allowNestedValues && NestedModelUtil.isNestedProperty(property)) {
      NestedModelUtil.setNestedValue(this, property, value);
    }
    return (X) map.put(property, value);
  }

  /**
   * Sets whether nested properties are enabled (defaults to true).
   *
   * @param allowNestedValues true to enable nested properties
   */
  public void setAllowNestedValues(boolean allowNestedValues) {
    this.allowNestedValues = allowNestedValues;
  }

  /**
   * Sets the properties.
   *
   * @param properties the properties
   */
  public void setProperties(Map<String, Object> properties) {
    for (String property : properties.keySet()) {
      set(property, properties.get(property));
    }
  }

    public String getNestedPrefix() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNestedPrefix(String nestedPrefix) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ModelData getNestedModel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNestedModel(ModelData nestedModel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSrcField(String srcField) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

