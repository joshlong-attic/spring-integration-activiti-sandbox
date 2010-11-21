package com.joshlong.activiti.coordinator.aop;

import com.joshlong.activiti.coordinator.annotations.StartProcess;
import com.joshlong.activiti.coordinator.annotations.ProcessVariable;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link org.aopalliance.intercept.MethodInterceptor} that starts a business process
 * as a result of a sucessful method invocation.
 *
 * @author Josh Long
 *
 */
public class ProcessStartingMethodInterceptor implements MethodInterceptor {

	/**
	 * injected reference - can be obtained via a {@link org.activiti.engine.impl.cfg.spring.ProcessEngineFactoryBean}
	 */
	protected ProcessEngine processEngine ;

	/**
	 * @param processEngine takes a reference to a {@link org.activiti.engine.ProcessEngine}
	 */
	public ProcessStartingMethodInterceptor(ProcessEngine processEngine) {
		this.processEngine = processEngine;
	}

	boolean shouldReturnProcessInstance( StartProcess startProcess,MethodInvocation methodInvocation , Object result){
		return startProcess.returnProcessInstance() && (result instanceof ProcessInstance ||
				methodInvocation.getMethod().getReturnType().isAssignableFrom(ProcessInstance.class));
	}

	boolean shouldReturnProcessInstanceId( StartProcess startProcess, MethodInvocation methodInvocation, Object result){
		return startProcess.returnProcessInstanceId() && (result instanceof String ||
			methodInvocation.getMethod().getReturnType().isAssignableFrom(String.class) );
	}

	boolean shouldReturnAsyncResultWithProcessInstanceId (StartProcess startProcess, MethodInvocation methodInvocation , Object result) {
		return startProcess.returnProcessInstanceFuture() &&
				(result instanceof AsyncResult|| methodInvocation.getMethod().getReturnType().isAssignableFrom(AsyncResult.class));
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {

		Method method = invocation.getMethod();

		StartProcess startProcess = AnnotationUtils.getAnnotation ( method , StartProcess.class );

		String processKey = startProcess.processKey();

		Assert.hasText( processKey , "you must provide the name of process to start");

		Object result ;
		try {
			result = invocation.proceed() ;
			Map<String,Object> vars = this.processVariablesFromAnnotations( invocation );
			System.out.println( vars.toString());
			RuntimeService runtimeService = this.processEngine.getRuntimeService();
			ProcessInstance pi = runtimeService.startProcessInstanceByKey( processKey , vars);
			String pId = pi.getId() ;

			if( invocation.getMethod().getReturnType().equals(void.class))
			return null; 

			if(shouldReturnProcessInstance(startProcess, invocation , result))
				return pi;

			if(shouldReturnProcessInstanceId(startProcess, invocation , result))
				return pId;

		} catch (Throwable th){
			throw new RuntimeException( th) ;
		}
		return result ;
	}

	/**
	 * if there any arguments with the {@link com.joshlong.activiti.coordinator.annotations.ProcessVariable} annotation,
	 * then we feed those parameters into the business process
	 * @param invocation  the invocation of the method as passed to the {@link org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)} method
	 * @return returns the map of process variables extracted from the parameters
	 * @throws Throwable thrown anything goes wrong
	 */
	protected Map<String,Object> processVariablesFromAnnotations( MethodInvocation invocation ) throws Throwable {
		Method method = invocation.getMethod() ;
		Annotation [][] annotations = method.getParameterAnnotations();
		Map<String,Object> vars = new HashMap<String,Object>() ;
		int paramIndx = 0 ;
		for(Annotation[] annPerParam : annotations ){

			for(Annotation annotation : annPerParam){
				if(annotation instanceof ProcessVariable){
					ProcessVariable processVariable = (ProcessVariable) annotation ;
					vars.put( processVariable.value(), invocation.getArguments()[paramIndx] );
				}
			}
			paramIndx+=1 ;
		}
		return vars;
	}
}