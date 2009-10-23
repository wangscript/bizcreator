/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.data;

import com.bizcreator.util.ObjectUtil;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author lgh
 */
public class MapModel implements Model {

    protected transient ChangeEventSupport changeEventSupport;
    protected boolean allowNestedValues = false;
    protected String nestedPrefix; //当前设值的嵌套字段
    protected ModelData nestedModel;
    protected String srcField;
    protected Map<String, Object> map;

    protected MapModel() {
        changeEventSupport = new ChangeEventSupport();
    }

    /**
     * Creates a new model with the given properties.
     *
     * @param properties the initial properties
     */
    public MapModel(Map<String, Object> properties) {
        this();
        setProperties(properties);
    }

    @SuppressWarnings("unchecked")
    public <X> X get(String property) {
        if (NestedModelUtil.isNestedProperty(property)) {
            if (allowNestedValues) {
                return (X) NestedModelUtil.getNestedValue(this, property);
            }
            else {
                int pos = property.indexOf(".");
                Object obj = get(property.substring(0, pos));
                return obj == null ? null : (X) ObjectUtil.getFieldValue(obj, property.substring(pos+1));
            }
        }
        return map == null ? null : (X) map.get(property);
    }

    public static void main(String[] args) {
        String s = "aa.c.b";
        int pos = s.indexOf(".");
        System.out.println(">>>aaa: " + pos + ", " + s.substring(0, pos) + ", " + s.substring(pos+1));
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
        Map<String, Object> newMap = new HashMap<String, Object>();
        if (map != null) {
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
    public <X> X remove(String name) {
        //return map == null ? null : (X) map.remove(property);
        if (map != null && map.containsKey(name)) {
            X oldValue = (X) map.remove(name);
            notifyPropertyChanged(name, null, oldValue);
            return oldValue;
        }
        return null;
    }

    /**
     * Sets the property and fires an <i>Update</i> event.
     *
     * @param property the property name
     * @param value the property value
     */
    @SuppressWarnings("unchecked")
    public <X> X set(String name, X value) {
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        X oldValue = null;
        if (NestedModelUtil.isNestedProperty(name)) {
            if (allowNestedValues) {
                NestedModelUtil.setNestedValue(this, name, value);
                oldValue = (X) map.put(name, value);
            }
            else {
                oldValue = (X)get(name);
                int pos = name.indexOf(".");
                Object obj = get(name.substring(0, pos));
                if (obj != null) ObjectUtil.setFieldValue(obj, name.substring(pos+1), value);
            }
        }
        else {
            oldValue = (X) map.put(name, value);
        }
        
        
        notifyPropertyChanged(name, value, oldValue);

        //下列代码解决在嵌套字段发生变化时，需要在嵌套对象中计算其他字段的值，其他字段的值发生变化
        if (nestedModel != null && nestedPrefix != null && !name.equals(srcField)
                && nestedPrefix.length() > 0 && name.indexOf(nestedPrefix) == -1) {

            //System.out.println(">>>fire from nested model: " + nestedPrefix + "." + property + value + ", " + oldValue);
            ((BeanModel)nestedModel).notifyPropertyChanged(nestedPrefix + "." + name, value, oldValue);

        }

        return oldValue;
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

    public Map getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
    
    //-------------------------------
    public String getNestedPrefix() {
        return nestedPrefix;
    }

    public void setNestedPrefix(String nestedPrefix) {
        this.nestedPrefix = nestedPrefix;
    }

     public ModelData getNestedModel() {
         return nestedModel;
     }

    public void setNestedModel(ModelData nestedModel) {
        this.nestedModel = nestedModel;
    }

    public void setSrcField(String srcField) {
        this.srcField = srcField;
    }

    public void addChangeListener(ChangeListener... listener) {
        changeEventSupport.addChangeListener(listener);
    }


    public void addChangeListener(List<ChangeListener> listeners) {
        for (ChangeListener listener : listeners) {
            changeEventSupport.addChangeListener(listener);
        }
    }

    public void notify(ChangeEvent evt) {
        changeEventSupport.notify(evt);
    }


    public void removeChangeListener(ChangeListener... listener) {
        changeEventSupport.removeChangeListener(listener);
    }

    public void removeChangeListeners() {
        changeEventSupport.removeChangeListeners();
    }

    public void setSilent(boolean silent) {
        changeEventSupport.setSilent(silent);
    }

    protected void fireEvent(int type) {
        notify(new ChangeEvent(type, this));
    }

    protected void fireEvent(int type, Model item) {
        notify(new ChangeEvent(type, this, item));
    }

    public void notifyPropertyChanged(String name, Object value, Object oldValue) {
        if (value == oldValue) {
            return;
        }
        if (value != null && value.equals(oldValue)) {
            return;
        }

        //System.out.println(">>>notify change: " + name + "(" + oldValue +", " + value + ")");

        notify(new PropertyChangeEvent(Update, this, name, oldValue, value));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        BeanModel result = (BeanModel) super.clone();
        result.changeEventSupport = new ChangeEventSupport();

        return result;
    }
}
