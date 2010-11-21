package com.joshlong.activiti.coordinator.aop;

import com.joshlong.activiti.coordinator.annotations.StartProcess;
import org.activiti.engine.ProcessEngine;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;

import java.io.Serializable;

/**
 * configures logic to support the creation of a business process on the completion of successful
 * method invocations with the {@link com.joshlong.activiti.coordinator.annotations.StartProcess}
 * annotation.
 * 
 * @author Josh Long
 * @since 1.0
 */
public class ProcessStartingPointcutAdvisor implements PointcutAdvisor, Serializable {

	/**
	 * the {@link org.aopalliance.intercept.MethodInterceptor} that handles launching the business process.
	 */
	protected MethodInterceptor advice;

	/**
	 * matches any method containing the {@link com.joshlong.activiti.coordinator.annotations.StartProcess} annotation.
	 */
	protected Pointcut pointcut ;

	/**
	 * the injected reference to the {@link org.activiti.engine.ProcessEngine}
	 */
	protected ProcessEngine processEngine ;

	public ProcessStartingPointcutAdvisor(ProcessEngine pe) {
		this.processEngine = pe;
		this.pointcut = buildPointcut();
		this.advice = buildAdvise();

	}

	protected MethodInterceptor buildAdvise(){
		return new ProcessStartingMethodInterceptor(this.processEngine );
	}

	protected Pointcut buildPointcut(){
		ComposablePointcut result = new ComposablePointcut();
		return result.union( new AnnotationMethodMatcher( StartProcess.class));
	}

	public Pointcut getPointcut() {
		return pointcut ;
	}

	public Advice getAdvice() {
		return advice ;
	}

	public boolean isPerInstance() {
		return true ;
	}
}

