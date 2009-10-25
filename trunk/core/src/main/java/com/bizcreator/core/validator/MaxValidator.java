/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.validator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.hibernate.validator.Max;
import org.hibernate.validator.Validator;

/**
 *
 * @author Administrator
 */
public class MaxValidator implements Validator<Max>, Serializable {

    public MaxValidator() {
        
    }

    public MaxValidator(long max) {
        this.max = max;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }
    protected long max;

    public void initialize(Max parameters) {
        max = parameters.value();
    }

    public boolean isValid(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            try {
                return new BigDecimal((String) value).compareTo(BigDecimal.valueOf(max)) <= 0;
            } catch (NumberFormatException nfe) {
                return false;
            }
        } else if ((value instanceof Double) || (value instanceof Float)) {
            double dv = ((Number) value).doubleValue();
            return dv <= max;
        } else if (value instanceof BigInteger) {
            return ((BigInteger) value).compareTo(BigInteger.valueOf(max)) <= 0;
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo(BigDecimal.valueOf(max)) <= 0;
        } else if (value instanceof Number) {
            long lv = ((Number) value).longValue();
            return lv <= max;
        } else {
            return false;
        }
    }
}
