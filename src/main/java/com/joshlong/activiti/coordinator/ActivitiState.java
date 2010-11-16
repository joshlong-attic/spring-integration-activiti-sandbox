package com.joshlong.activiti.coordinator;

import java.lang.annotation.*;

/**
 * indicates that a method is to be enlisted as a handler for a given BPMN state
 *
 * @author Josh Long
 * @since 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented//@Component
public @interface ActivitiState {
	/**
	 * the state that the component responds to 
	 */
	String value() default "";

}
