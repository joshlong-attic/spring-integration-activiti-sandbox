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
package com.joshlong.activiti.coordinator.registration1.distribution.consumer;

import com.joshlong.activiti.coordinator.CoordinatorGatewayClient;
import com.joshlong.activiti.coordinator.aop.ActivitiStateAnnotationBeanPostProcessor;
import com.joshlong.activiti.coordinator.registration1.distribution.SharedConfiguration;
import com.joshlong.activiti.coordinator.registry.ActivitiStateHandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RegistrationConsumerConfiguration  extends SharedConfiguration {
	@Bean
	public ActivitiStateHandlerRegistry registry() {
		return new ActivitiStateHandlerRegistry();
	}

	@Bean
	public CoordinatorGatewayClient coordinatorGatewayClient() {
		CoordinatorGatewayClient client = new CoordinatorGatewayClient();
		client.setRegistry(this.registry());
		return client;
	}

	@Bean
	public ActivitiStateAnnotationBeanPostProcessor activitiStateAnnotationBeanPostProcessor() {
		ActivitiStateAnnotationBeanPostProcessor activitiStateAnnotationBeanPostProcessor =
				new ActivitiStateAnnotationBeanPostProcessor();
		activitiStateAnnotationBeanPostProcessor.setRegistry(this.registry());
		return activitiStateAnnotationBeanPostProcessor;
	}
}
