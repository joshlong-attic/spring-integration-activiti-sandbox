package com.joshlong.activiti.coordinator;

import org.activiti.engine.DbSchemaStrategy;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.cfg.spring.ProcessEngineFactoryBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;

import org.springframework.integration.MessageChannel;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import org.springframework.stereotype.Component;

import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Component
public class TestConfiguration {

    @Value("#{ds}")
    private DataSource dataSource;

    @Value("#{response}")
    private MessageChannel response;

    @Value("#{request}")
    private MessageChannel request;

    @Autowired
    private ProcessEngine processEngine;

    @Bean
    public OutboundActivitiCoodinatorGateway gateway() {
        OutboundActivitiCoodinatorGateway outboundActivitiCoodinatorGateway = new OutboundActivitiCoodinatorGateway();
        outboundActivitiCoodinatorGateway.setReplyChannel(this.response);
        outboundActivitiCoodinatorGateway.setRequestChannel(this.request);
        outboundActivitiCoodinatorGateway.setProcessEngine(this.processEngine);
        outboundActivitiCoodinatorGateway.setForwardProcessVariablesAsMessageHeaders(true);
        outboundActivitiCoodinatorGateway.setUpdateProcessVariablesFromReplyMessageHeaders(true);
        return outboundActivitiCoodinatorGateway;
    }

    @Bean
    public DataSource dataSource() {
        return new TransactionAwareDataSourceProxy(this.dataSource);
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
        pe.setDbSchemaStrategy(DbSchemaStrategy.CREATE);
        return pe;
    }
}
