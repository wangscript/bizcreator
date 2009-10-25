/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.validator;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.hibernate.validator.Digits;
import org.hibernate.validator.Validator;

/**
 *
 * @author Administrator
 */
public class DigitsValidator implements Validator<Digits> {

    public DigitsValidator() {
        super();
    }

    public DigitsValidator(int id, int fd) {
        this.integerDigits = id;
        this.fractionalDigits = fd;
    }

    public DigitsValidator(int id) {
        this.integerDigits = id;
        this.fractionalDigits = 0;
    }

    public int getFractionalDigits() {
        return fractionalDigits;
    }

    public void setFractionalDigits(int fractionalDigits) {
        this.fractionalDigits = fractionalDigits;
    }

    public int getIntegerDigits() {
        return integerDigits;
    }

    public void setIntegerDigits(int integerDigits) {
        this.integerDigits = integerDigits;
    }
    protected int integerDigits;
    protected int fractionalDigits;

    public void initialize(Digits configuration) {
        integerDigits = configuration.integerDigits();
        fractionalDigits = configuration.fractionalDigits();
    }

    public boolean isValid(Object value) {
        if (value == null) {
            return true;
        }

        String stringValue = null;

        if (value instanceof String) {
            try {
                stringValue = stringValue(new BigDecimal((String) value));
            } catch (NumberFormatException nfe) {
                return false;
            }
        } else if (value instanceof BigDecimal) {
            stringValue = stringValue((BigDecimal) value);
        } else if (value instanceof BigInteger) {
            stringValue = stringValue((BigInteger) value);
        } else if (value instanceof Number) {
            //yukky
            stringValue = stringValue(new BigDecimal(((Number) value).toString()));
        } else {
            return false;
        }

        int pos = stringValue.indexOf(".");

        int left = (pos == -1) ? stringValue.length() : pos;
        int right = (pos == -1) ? 0 : stringValue.length() - pos - 1;

        if (left == 1 && stringValue.charAt(0) == '0') {
            left--;
        }

        return !(left > integerDigits || right > fractionalDigits);

    }

    private String stringValue(BigDecimal number) {
        return number.abs().stripTrailingZeros().toPlainString();
    }

    private String stringValue(BigInteger number) {
        return number.abs().toString();
    }
}
