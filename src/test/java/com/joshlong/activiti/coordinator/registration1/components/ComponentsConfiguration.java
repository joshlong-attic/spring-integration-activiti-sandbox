package com.joshlong.activiti.coordinator.registration1.components;

import com.joshlong.activiti.coordinator.aop.ProcessStartAnnotationBeanPostProcessor;
import com.joshlong.activiti.coordinator.registration1.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class ComponentsConfiguration extends ProcessEngineConfiguration {

	@Autowired ProcessEngine processEngine;

	@Override
	protected Resource[] processResources() {
		 	return new Resource[]{new ClassPathResource("processes/registration1.bpmn20.xml")};
	}

	@Bean
	public ProcessStartAnnotationBeanPostProcessor processStartAnnotationBeanPostProcessor(){
		ProcessStartAnnotationBeanPostProcessor  processStartAnnotationBeanPostProcessor
					= new ProcessStartAnnotationBeanPostProcessor ();
		processStartAnnotationBeanPostProcessor.setProcessEngine(this.processEngine);
		return processStartAnnotationBeanPostProcessor ;
	}

	
}
