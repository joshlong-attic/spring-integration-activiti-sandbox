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
package com.joshlong.activiti.coordinator;

import com.joshlong.activiti.coordinator.registry.ActivitiStateHandlerRegistration;
import com.joshlong.activiti.coordinator.registry.ActivitiStateHandlerRegistry;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.MessagingException;
import org.springframework.integration.support.MessageBuilder;

import java.lang.reflect.Method;
import java.util.*;


/**
 * handles coordinating inbound messages that have come from the {@link CoordinatorGatewayProducer}
 *
 * @author Josh Long
 * @since 1.0
 */
public class CoordinatorGatewayClient implements ApplicationContextAware {
	private volatile ActivitiStateHandlerRegistry registry;
	private ApplicationContext applicationContext;

	public void setRegistry(ActivitiStateHandlerRegistry registry) {
		this.registry = registry;
	}

	public Message<?> processMessage(Message<?> message) throws MessagingException {
		try {
			MessageHeaders headers = message.getHeaders();

			String procName = (String) headers.get(CoordinatorConstants.PROCESS_NAME);

			String stateName = (String) headers.get(CoordinatorConstants.STATE_NAME);

			ActivitiStateHandlerRegistration registration = registry.findRegistrationForProcessAndState( procName, stateName);

			Object bean = applicationContext.getBean(registration.getBeanName());

			Method method = registration.getHandlerMethod();
//			int size = method.getParameterTypes().length;
			ArrayList<Object> argsList = new ArrayList<Object>();

			Map<Integer, String> processVariablesMap = registration.getProcessVariablesExpected();

			// already has which indexes get which process variables
			Map<Integer, Object> variables = new HashMap<Integer, Object>();

			for (Integer i : processVariablesMap.keySet())
				variables.put(i, headers.get(processVariablesMap.get(i)));

			if (registration.requiresProcessId()) {
				variables.put(registration.getProcessIdIndex(),
						headers.get(CoordinatorConstants.PROC_ID));
			}

//			System.out.println(variables.toString());

			List<Integer> indices = new ArrayList<Integer>(variables.keySet());
			Collections.sort(indices);

			argsList.clear();

			for (Integer idx : indices)
				argsList.add(variables.get(idx));

			Object[] args = argsList.toArray(new Object[argsList.size()]);
			Object result = (args.length == 0) ? method.invoke(bean)
					: method.invoke(bean, args);

			MessageBuilder builder = MessageBuilder.withPayload(message.getPayload())
					.copyHeaders(message.getHeaders());

			if (result instanceof Map) {
			  // then we update the BPM
				// by including them in the reply message
				builder.copyHeadersIfAbsent( (Map)result);
			}

			return builder.build();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}
