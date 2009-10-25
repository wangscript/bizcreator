/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.validator;

import com.bizcreator.core.ResourceManager;
import com.bizcreator.core.ResourceMap;
import com.bizcreator.util.ObjectUtil;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.hibernate.validator.Validator;
import org.hibernate.validator.ValidatorClass;

/**
 *
 * @author lgh
 */
public class BizValidators {

    public final static String INVALID_ROW_INDEX = "rowIndex";
    public final static String INVALID_COL_INDEX = "colIndex";
    public final static String INVALID_MESSAGE = "message";
    
    protected static Map<String, Class<? extends Validator>> validatorMap =
            new HashMap<String, Class<? extends Validator>>();

    static {
        validatorMap.put("DigitsValidator", DigitsValidator.class);
        validatorMap.put("EmailValidator", EmailValidator.class);
        validatorMap.put("LengthValidator", LengthValidator.class);

        validatorMap.put("MaxValidator", MaxValidator.class);
        validatorMap.put("MinValidator", MinValidator.class);
        validatorMap.put("PatternValidator", PatternValidator.class);
        validatorMap.put("RangeValidator", RangeValidator.class);
        validatorMap.put("SizeValidator", SizeValidator.class);
    }

    public static Class getValidatorClass(String key) {
        return validatorMap.get(key);
    }

    public static Validator createValidator(Annotation annotation) {
        try {
            ValidatorClass validatorClass = annotation.annotationType().getAnnotation(ValidatorClass.class);
            if (validatorClass == null) {
                return null;
            }
            Class<? extends Validator> clazz = validatorClass.value();
            String validName = clazz.getSimpleName();
            if (getValidatorClass(validName) != null) {
                clazz = getValidatorClass(validName);
            }

            Validator beanValidator = clazz.newInstance();
            beanValidator.initialize(annotation);
            //defaultInterpolator.addInterpolator(annotation, beanValidator) ;
            return beanValidator;
        } catch (Exception e) {
            throw new IllegalArgumentException("could not instantiate ClassValidator", e);
        }
    }


    private static ResourceMap resourceMap;
    public static String getInvalidMessage(Validator validator) {
        if (resourceMap == null) {
            resourceMap = ResourceManager.instance().getResourceMap(ValidatorMessages.class);
        }
        String message = resourceMap.getString(validator.getClass().getSimpleName() + ".message");
        StringTokenizer tokens = new StringTokenizer(message, "#{}", true);
        StringBuilder buf = new StringBuilder(30);

        boolean escaped = false;
        boolean el = false;

        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            if (!escaped && "#".equals(token)) {
                el = true;
            }
            if (!el && "{".equals(token)) {
                escaped = true;
            } else if (escaped && "}".equals(token)) {
                escaped = false;
            } else if (!escaped) {
                if ("{".equals(token)) {
                    el = false;
                }
                buf.append(token);
            } else {
                Object variable = ObjectUtil.getFieldValue(validator, token);
                if (variable != null) {
                    buf.append(variable);
                } else {
                    buf.append('{').append(token).append('}');
                }
            }
        }
        return buf.toString();
    }
}
