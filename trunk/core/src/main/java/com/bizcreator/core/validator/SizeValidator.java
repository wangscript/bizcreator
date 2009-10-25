/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.validator;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import org.hibernate.validator.Size;
import org.hibernate.validator.Validator;

/**
 *
 * @author Administrator
 */
public class SizeValidator implements Validator<Size>, Serializable {
    
    public SizeValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }
    
    public SizeValidator() {
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

	public void initialize(Size parameters) {
		max = parameters.max();
		min = parameters.min();
	}

	public boolean isValid(Object value) {
		if ( value == null ) return true;
		int length;
		if ( value.getClass().isArray() ) {
			length = Array.getLength( value );
		}
		else if ( value instanceof Collection ) {
			length = ( (Collection) value ).size();
		}
		else if ( value instanceof Map ) {
			length = ( (Map) value ).size();
		}
		else {
			return false;
		}
		return length >= min && length <= max;
	}
}
