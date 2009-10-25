/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.jasper;

import com.bizcreator.util.ObjectUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;

/**
 *
 * @author lgh
 */
public abstract class RhAbstractBeanDataSource implements JRRewindableDataSource {

    /**
     * Field mapping that produces the current bean.
     * <p/>
     * If the field name/description matches this constant (the case is important),
     * the data source will return the current bean as the field value.
     */
    public static final String CURRENT_BEAN_MAPPING = "_THIS";
    protected boolean isUseFieldDescription = false;

    public RhAbstractBeanDataSource(boolean isUseFieldDescription) {
        this.isUseFieldDescription = isUseFieldDescription;
    }

    protected Object getFieldValue(Object bean, JRField field) throws JRException {
        return getBeanProperty(bean, getPropertyName(field));
    }

    protected static Object getBeanProperty(Object bean, String propertyName) throws JRException {
        Object value = null;

        if (isCurrentBeanMapping(propertyName)) {
            value = bean;
        } else {
            value = ObjectUtil.getFieldValue(bean, propertyName);
        }
        return value;
    }

    protected static boolean isCurrentBeanMapping(String propertyName) {
        return CURRENT_BEAN_MAPPING.equals(propertyName);
    }

    protected String getPropertyName(JRField field) {
        if (isUseFieldDescription) {
            if (field.getDescription() == null) {
                return field.getName();
            }
            return field.getDescription();
        } else {
            return field.getName();
        }
    }
}
