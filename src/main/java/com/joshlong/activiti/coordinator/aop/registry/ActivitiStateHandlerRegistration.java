package com.joshlong.activiti.coordinator.aop.registry;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * an instance of a bean discovered to both have an {@link com.joshlong.activiti.coordinator.annotations.ActivitiHandler}
 * and one or more {@link com.joshlong.activiti.coordinator.annotations.ActivitiHandler} annotations present.
 * <p/>
 * Describes the metadata extracted from the bean at configuration time
 *
 * @author Josh Long
 * @since 1.0
 */
public class ActivitiStateHandlerRegistration {

    private Map<Integer,String> processVariablesExpected = new ConcurrentHashMap<Integer,String>();
    private Method handlerMethod;
    private Object handler;
    private String stateName;
    private String beanName ;
    private int processVariablesIndex = -1;
    private int processIdIndex = -1;
    private String processName ;

    public ActivitiStateHandlerRegistration(Map<Integer, String> processVariablesExpected, Method handlerMethod, Object handler, String stateName, String beanName, int processVariablesIndex, int processIdIndex, String processName) {
        this.processVariablesExpected = processVariablesExpected;
        this.handlerMethod = handlerMethod;
        this.handler = handler;
        this.stateName = stateName;
        this.beanName = beanName;
        this.processVariablesIndex = processVariablesIndex;
        this.processIdIndex = processIdIndex;
        this.processName = processName;
    }

    public int getProcessVariablesIndex() {

        return processVariablesIndex;
    }

    public int getProcessIdIndex() {
        return processIdIndex;
    }

    public boolean requiresProcessId(){
        return this.processIdIndex > -1;
    }
    public boolean requiresProcessVariablesMap(){
        return processVariablesIndex >-1;
    }

    public String getBeanName() {
        return beanName;
    }

    public Map<Integer, String> getProcessVariablesExpected() {
        return processVariablesExpected;
    }

    public Method getHandlerMethod() {
        return handlerMethod;
    }

    public Object getHandler() {
        return handler;
    }

    public String getStateName() {
        return stateName;
    }

    public String getProcessName() {
        return processName;
    }

    @Override
    public String toString() {
     return ToStringBuilder.reflectionToString(this);
    }

    public String getRegistrationKey(){
        return org.apache.commons.lang.StringUtils.defaultString(this.stateName)+":"+
                org.apache.commons.lang.StringUtils.defaultString( this.processName);
    }

}
