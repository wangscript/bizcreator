package com.bizcreator.core.data;

import com.bizcreator.util.ObjectUtil;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.persistence.Transient;

public class BeanModel implements Model {

    protected transient ChangeEventSupport changeEventSupport;
    protected transient ChangeListener changeListener;
    
    protected boolean allowNestedValues = true;
    protected String nestedPrefix; //当前设值的嵌套字段
    protected ModelData nestedModel;
    protected String srcField;
    protected Map extPropertiess;

    protected BeanModel() {
        changeEventSupport = new ChangeEventSupport();
        //pcs = new PropertyChangeSupport(this);
        //vcs = new VetoableChangeSupport(this);
    }

    protected void onModelChange(String property, Object oldValue, Object value ) {
        //
    }

    /**
     * @inheritDoc
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        BeanModel result = (BeanModel) super.clone();
        //result.pcs = new PropertyChangeSupport(result);
        //result.vcs = new VetoableChangeSupport(result);
        result.changeEventSupport = new ChangeEventSupport();
        result.changeListener = new ChangeListener() {
            public void modelChanged(ChangeEvent event) {
                PropertyChangeEvent pce = (PropertyChangeEvent) event;
                onModelChange(pce.getName(), pce.getOldValue(), pce.getNewValue());
            }
        };
        result.addChangeListener(result.changeListener);
        return result;
    }

    //----------------------------Implements Model Interface ------------------
    

    public boolean isAllowNestedValues() {
        return allowNestedValues;
    }

    
    public void setAllowNestedValues(boolean allowNestedValues) {
        this.allowNestedValues = allowNestedValues;
    }

    @Transient
    public String getNestedPrefix() {
        return nestedPrefix;
    }

    public void setNestedPrefix(String nestedPrefix) {
        this.nestedPrefix = nestedPrefix;
    }

    @Transient
     public ModelData getNestedModel() {
         return nestedModel;
     }

    public void setNestedModel(ModelData nestedModel) {
        this.nestedModel = nestedModel;
    }

    public void setSrcField(String srcField) {
        this.srcField = srcField;
    }

    @Transient
    @SuppressWarnings("unchecked")
    public <X> X get(String property) {
        if (allowNestedValues && NestedModelUtil.isNestedProperty(property)) {
            return (X) NestedModelUtil.getNestedValue(this, property);
        }
        return (X) ObjectUtil.getFieldValue(this, property);
    }

    
    @Transient
    @SuppressWarnings("unchecked")
    public <X> X get(String property, X valueWhenNull) {
        X value = (X) get(property);
        return (value == null) ? valueWhenNull : value;
    }

    @SuppressWarnings("unchecked")
    public <X> X set(String property, X value) {
        //System.out.println(">>>set property : " + property + " = " + value);
        if (changeListener == null) {
            changeListener = new ChangeListener() {
                public void modelChanged(ChangeEvent event) {
                    PropertyChangeEvent pce = (PropertyChangeEvent) event;
                    onModelChange(pce.getName(), pce.getOldValue(), pce.getNewValue());
                }
            };
            addChangeListener(changeListener);
        }
        Object oldValue = ObjectUtil.getFieldValue(this, property);
        if (allowNestedValues && NestedModelUtil.isNestedProperty(property)) {
            NestedModelUtil.setNestedValue(this, property, value);
        }
        else {
            oldValue = ObjectUtil.setFieldValue(this, property, value);
        }
        notifyPropertyChanged(property, value, oldValue);


        //System.out.println(">>>property: " + property);
        //System.out.println(">>>" + nestedPrefix + ", " + nestedModel);
        //下列代码解决在嵌套字段发生变化时，需要在嵌套对象中计算其他字段的值，其他字段的值发生变化
        if (nestedModel != null && nestedPrefix != null && !property.equals(srcField)
                && nestedPrefix.length() > 0 && property.indexOf(nestedPrefix) == -1) {

            //System.out.println(">>>fire from nested model: " + nestedPrefix + "." + property + value + ", " + oldValue);
            ((BeanModel)nestedModel).notifyPropertyChanged(nestedPrefix + "." + property, value, oldValue);

        }
        return (X) oldValue;
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

    @Transient
    public Map<String, Object> getProperties() {
        return null;
    }

    @Transient
    public Collection<String> getPropertyNames() {
        return null;
    }

    public <X> X remove(String name) {
        return null;
    }

    @Transient
    public Map getExtPropertiess() {
        return extPropertiess;
    }

    public void setExtPropertiess(Map extPropertiess) {
        this.extPropertiess = extPropertiess;
    }


    //---------------------- 事件处理 ----------------
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
        notify(new PropertyChangeEvent(Update, this, name, oldValue, value));
    }

}