package com.joshlong.activiti.coordinator.registration1;

import com.joshlong.activiti.coordinator.CoordinatorGatewayClient;
import com.joshlong.activiti.coordinator.CoordinatorGatewayProducer;
import com.joshlong.activiti.coordinator.aop.ActivitiStateAnnotationBeanPostProcessor;
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

import org.springframework.stereotype.Component;

import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration 
public class RegistrationConfiguration {

    @Value("#{reply}")
    private MessageChannel reply;

    @Value("#{request}")
    private MessageChannel request;

    @Autowired
    private ProcessEngine processEngine;

    @Bean
    public CoordinatorGatewayProducer gateway() {
        CoordinatorGatewayProducer coordinatorGatewayProducer = new CoordinatorGatewayProducer();
        coordinatorGatewayProducer.setReplyChannel(this.reply);
        coordinatorGatewayProducer.setRequestChannel(this.request);
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

    @Bean
    public ActivitiStateHandlerRegistry registry(){
        ActivitiStateHandlerRegistry activitiStateHandlerRegistry = new ActivitiStateHandlerRegistry ();
        return activitiStateHandlerRegistry ;
    }

    @Bean
    public CoordinatorGatewayClient coordinatorGatewayClient (){
        CoordinatorGatewayClient client = new CoordinatorGatewayClient();
        client.setRegistry( this.registry()) ;
        return client;
    }

    @Bean
    public ActivitiStateAnnotationBeanPostProcessor activitiStateAnnotationBeanPostProcessor (){
        ActivitiStateAnnotationBeanPostProcessor activitiStateAnnotationBeanPostProcessor =
                new ActivitiStateAnnotationBeanPostProcessor ();
        activitiStateAnnotationBeanPostProcessor.setRegistry(this.registry());
        return activitiStateAnnotationBeanPostProcessor;
    }
}
