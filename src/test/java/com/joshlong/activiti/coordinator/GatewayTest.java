package com.joshlong.activiti.coordinator;

import org.activiti.engine.ProcessEngine;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;


@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class GatewayTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private ProcessEngine processEngine;

    @Test
    public void testGateway() throws Throwable {
        processEngine.getRepositoryService().createDeployment()
                     .addClasspathResource("processes/si_gateway_example.bpmn20.xml")
                     .deploy();

        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("customerId", 232);
        processEngine.getRuntimeService()
                     .startProcessInstanceByKey("sigatewayProcess", vars);
        Thread.sleep(10 * 1000);
    }
}
