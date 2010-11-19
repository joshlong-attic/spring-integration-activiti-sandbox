package com.joshlong.activiti.coordinator.registration1;

import org.activiti.engine.ProcessEngine;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * simple example demonstratong the deployment of the activiti components
 *
 * @since  1.0
 * @author Josh Long
 *
 */
public class RegistrationMain {

	public static void main (String[] args) throws Throwable {

		ClassPathXmlApplicationContext cax = new ClassPathXmlApplicationContext(
                "com/joshlong/activiti/coordinator/registration1.xml") ;

        ProcessEngine processEngine = cax.getBean(ProcessEngine.class);

        Map<String, Object> vars = new HashMap<String, Object>() ;
        vars.put( "customerId", 23) ;
        processEngine.getRuntimeService().startProcessInstanceByKey( "customer-signup-process",vars);

        Thread.sleep(1000 * 30 );

	}
}

