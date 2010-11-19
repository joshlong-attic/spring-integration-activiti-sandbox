package com.joshlong.activiti.coordinator.aop.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * this class records and manages all known {@link com.joshlong.activiti.coordinator.annotations.ActivitiState} - responding
 * beans in the JVM. It <em>should</em> have metadata on all methods, and what
 * those methods expect from a given invocation (ie: which process, which process variables).
 *
 * @author Josh Long
 * @since 1.0
 */
public class ActivitiStateHandlerRegistry {

    private volatile Map<String,ActivitiStateHandlerRegistration> registrations =
            new ConcurrentHashMap<String, ActivitiStateHandlerRegistration>();

    public void registerActivitiStateHandler(ActivitiStateHandlerRegistration registration) {
        this.registrations.put( registration.getRegistrationKey(), registration);
    }
}
