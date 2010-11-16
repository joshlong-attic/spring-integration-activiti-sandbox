package com.joshlong.activiti.coordinator;

import java.lang.annotation.*;


/**
 * Annotation used on method signatures to tell the runtime to
 * inject the current process' process variables as a {@link java.util.Map<String,Object>}. 
 *
 * @author Josh Long
 * @since 1.0
 *
 */

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProcessVariable {

	/**
	 * Expression for matching against nested properties of the payload.
	 */
	String value() default "";

}
