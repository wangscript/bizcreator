/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.validator;

import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.validator.Range;
import org.hibernate.validator.Validator;

/**
 *
 * @author Administrator
 */
public class RangeValidator implements Validator<Range>, Serializable {

    public RangeValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public RangeValidator() {
        this.min = Long.MIN_VALUE;
        this.max = Long.MAX_VALUE;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }
    protected long max;
    protected long min;

    public void initialize(Range parameters) {
        max = parameters.max();
        min = parameters.min();
    }

    public boolean isValid(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            try {
                BigDecimal dv = new BigDecimal((String) value);
                return dv.compareTo(BigDecimal.valueOf(min)) >= 0 && dv.compareTo(BigDecimal.valueOf(max)) <= 0;
            } catch (NumberFormatException nfe) {
                return false;
            }
        } else if ((value instanceof Double) || (value instanceof Float)) {
            double dv = ((Number) value).doubleValue();
            return dv >= min && dv <= max;
        } else if (value instanceof Number) {
            long lv = ((Number) value).longValue();
            return lv >= min && lv <= max;
        } else {
            return false;
        }
    }
}
