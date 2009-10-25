/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.validator;

import java.io.Serializable;
import org.hibernate.validator.Length;
import org.hibernate.validator.Validator;

/**
 *
 * @author Administrator
 */
public class LengthValidator implements Validator<Length>, Serializable {

    public LengthValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public LengthValidator() {
        this.min = 0;
        this.max = Integer.MAX_VALUE;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
    protected int max;
    protected int min;

    public void initialize(Length parameters) {
        max = parameters.max();
        min = parameters.min();
    }

    public boolean isValid(Object value) {
        if (value == null) {
            return true;
        }
        if (!(value instanceof String)) {
            return false;
        }
        String string = (String) value;
        int length = string.length();
        return length >= min && length <= max;
    }
}
