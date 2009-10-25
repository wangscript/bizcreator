/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.validator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.hibernate.validator.Validator;

/**
 *
 * @author lgh
 */
public class GreatThanValidator implements Validator<GreatThan>, Serializable {

    private long min;

    public GreatThanValidator() {
    }

    public GreatThanValidator(long min) {
        this.min = min;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public void initialize(GreatThan parameters) {
        min = parameters.value();
    }

    public boolean isValid(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            try {
                return new BigDecimal((String) value).compareTo(BigDecimal.valueOf(min)) > 0;
            } catch (NumberFormatException nfe) {
                return false;
            }
        } else if ((value instanceof Double) || (value instanceof Float)) {
            double dv = ((Number) value).doubleValue();
            return dv > min;
        } else if (value instanceof BigInteger) {
            return ((BigInteger) value).compareTo(BigInteger.valueOf(min)) > 0;
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo(BigDecimal.valueOf(min)) > 0;
        } else if (value instanceof Number) {
            long lv = ((Number) value).longValue();
            return lv > min;
        } else {
            return false;
        }
    }
}
