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
package com.joshlong.activiti.coordinator.registration1.producer;

import org.activiti.engine.ProcessEngine;

import org.apache.commons.lang.StringUtils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * simple example demonstratong the deployment of the activiti components
 *
 * @author Josh Long
 * @since 1.0
 */
public class RegistrationProducerMain {
		static void doContinuousIterationUpUntil( ApplicationContext ctx, int counter) throws Throwable {
			ProcessEngine processEngine = ctx.getBean( ProcessEngine.class);
			for(int customerId = 0; customerId < counter; customerId++){
				Map<String, Object> vars = Collections.singletonMap("customerId", (Object) customerId);
				processEngine.getRuntimeService() .startProcessInstanceByKey("customer-fullfillment-process", vars);
			}
			while(true)
				Thread.sleep(10000);
		}

    static void doReadLine(ApplicationContext ctx) throws Throwable {
        ProcessEngine processEngine = ctx.getBean(ProcessEngine.class);

        String line;
        BufferedReader reader = new BufferedReader(
						new InputStreamReader( System.in));

        while (true) {
            System.out.println( "which customer would you like to start a fulfillment " +
																"process for? Enter it, then hit <Enter>:");
            line = reader.readLine();
            Integer customerId = Integer.parseInt( StringUtils.defaultString( line).trim());
            Map<String, Object> vars = Collections.singletonMap("customerId", (Object) customerId);
            processEngine.getRuntimeService() .startProcessInstanceByKey("customer-fullfillment-process", vars);
        }
    }
		public static void doDefault () throws Throwable {
			while(true) Thread.sleep(1000 * 10);
		}

    public static void main(String[] args) throws Throwable {

			ApplicationContext cax = new ClassPathXmlApplicationContext( "producer.xml");

		//	doDefault();
 		doContinuousIterationUpUntil( cax, 100);
    }
}
