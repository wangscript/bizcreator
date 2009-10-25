/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.validator;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import org.hibernate.validator.ValidatorClass;

@Documented
@ValidatorClass(LessThanValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface LessThan {
	long value();
	String message() default "{validator.lessThan}";
}
