/*
 * Copyright 2010 the original author or authors
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
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
				"registration1.xml") ;

        ProcessEngine processEngine = cax.getBean(ProcessEngine.class);

        Map<String, Object> vars = new HashMap<String, Object>() ;
        vars.put( "customerId", 23) ;
        processEngine.getRuntimeService().startProcessInstanceByKey( "customer-fullfillment-process",vars);

        Thread.sleep(1000 * 30 );

	}
}

