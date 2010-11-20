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

import com.joshlong.activiti.coordinator.CoordinatorGatewayClient;
import com.joshlong.activiti.coordinator.CoordinatorGatewayProducer;
import com.joshlong.activiti.coordinator.aop.ActivitiStateAnnotationBeanPostProcessor;
import com.joshlong.activiti.coordinator.registration1.SharedConfiguration;
import com.joshlong.activiti.coordinator.registry.ActivitiStateHandlerRegistry;
import org.activiti.engine.DbSchemaStrategy;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.cfg.spring.ProcessEngineFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.MessageChannel;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration
public class RegistrationProducerConfiguration extends SharedConfiguration {

    @Value("#{replies}")
    private MessageChannel replies;

    @Value("#{requests}")
    private MessageChannel requests;

    @Autowired
    private ProcessEngine processEngine;

    @Bean
    public CoordinatorGatewayProducer gateway() {
        CoordinatorGatewayProducer coordinatorGatewayProducer = new CoordinatorGatewayProducer();
        coordinatorGatewayProducer.setReplyChannel(this.replies);
        coordinatorGatewayProducer.setRequestChannel(this.requests);
        coordinatorGatewayProducer.setProcessEngine(this.processEngine);
        coordinatorGatewayProducer.setForwardProcessVariablesAsMessageHeaders(true);
        coordinatorGatewayProducer.setUpdateProcessVariablesFromReplyMessageHeaders(true);
        return coordinatorGatewayProducer;
    }

    @Bean
    public DataSource activitiDataSource(){
        return new SimpleDriverDataSource(
            new org.h2.Driver() ,"jdbc:h2:tcp://localhost/~/activiti_example3","sa","");
    }
    @Bean
    public DataSource dataSource() {
        return new TransactionAwareDataSourceProxy( activitiDataSource() );
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(this.dataSource());
    }

    @Bean
    public ProcessEngineFactoryBean processEngineFactoryBean() {
        ProcessEngineFactoryBean pe = new ProcessEngineFactoryBean();
        pe.setDataSource(this.dataSource());
        pe.setTransactionManager(this.platformTransactionManager());
        pe.setDbSchemaStrategy(DbSchemaStrategy.CHECK_VERSION);
        pe.setDeploymentResources( new Resource[]{new ClassPathResource("processes/registration.bpmn20.xml")} );
        return pe;
    }
}
