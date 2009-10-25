/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.validator;

import java.io.Serializable;
import java.util.regex.Matcher;
import org.hibernate.validator.Pattern;
import org.hibernate.validator.Validator;

/**
 *
 * @author Administrator
 */
public class PatternValidator implements Validator<Pattern>, Serializable {

    public PatternValidator() {
        super();
    }

    public PatternValidator(String regex, int flags) {
        pattern = java.util.regex.Pattern.compile(regex, flags);
    }

    public PatternValidator(String regex) {
        pattern = java.util.regex.Pattern.compile(regex, 0);
    }

    public java.util.regex.Pattern getPattern() {
        return pattern;
    }

    public void setPattern(java.util.regex.Pattern pattern) {
        this.pattern = pattern;
    }
    protected java.util.regex.Pattern pattern;

    public void initialize(Pattern parameters) {
        pattern = java.util.regex.Pattern.compile(
                parameters.regex(),
                parameters.flags());
    }

    public boolean isValid(Object value) {
        if (value == null) {
            return true;
        }
        if (!(value instanceof String)) {
            return false;
        }
        String string = (String) value;
        Matcher m = pattern.matcher(string);
        return m.matches();
    }
}
