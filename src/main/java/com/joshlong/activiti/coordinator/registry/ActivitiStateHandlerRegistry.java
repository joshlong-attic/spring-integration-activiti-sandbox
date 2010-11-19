package com.joshlong.activiti.coordinator.registry;

import java.util.ArrayList;
import java.util.Collection;
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

    private volatile Map<String, ActivitiStateHandlerRegistration> registrations =
            new ConcurrentHashMap<String, ActivitiStateHandlerRegistration>();

    protected String registrationKey(String stateName, String processName) {
        return (org.apache.commons.lang.StringUtils.defaultString(processName) + ":" +
                org.apache.commons.lang.StringUtils.defaultString(stateName)).toLowerCase();
    }

    public void registerActivitiStateHandler(ActivitiStateHandlerRegistration registration) {
        String regKey = registrationKey(registration.getProcessName(), registration.getStateName());
        this.registrations.put(regKey, registration);
    }

    /**
     * this is responsible for looking up components in the registry and returning the appropriate handler based
     * on specificity of the {@link ActivitiStateHandlerRegistration}
     *
     * @param processName the process name to look for (optional)
     * @param stateName   the state name to look for (not optional)
     * @return all matching options
     */
    public Collection<ActivitiStateHandlerRegistration> findRegistrationsForProcessAndState(String processName, String stateName) {
        Collection<ActivitiStateHandlerRegistration> registrationCollection = new ArrayList<ActivitiStateHandlerRegistration>();
        String regKey = registrationKey(processName, stateName);
        for (String k : this.registrations.keySet())
            if (k.contains(regKey))
                registrationCollection.add(this.registrations.get(k));
        return registrationCollection;
    }

    public ActivitiStateHandlerRegistration findRegistrationForProcessAndState(String pName, String stateName) {
        ActivitiStateHandlerRegistration r = null;
        String key = registrationKey(pName, stateName);
        Collection<ActivitiStateHandlerRegistration> rs = this.findRegistrationsForProcessAndState(pName, stateName);
        for (ActivitiStateHandlerRegistration sr : rs) {
            String kName = registrationKey(sr.getProcessName(), sr.getStateName());
            if (key.equalsIgnoreCase(kName)) {
                r = sr;
                break;
            }
        }
        if (r == null && rs.size() > 0)
            return rs.iterator().next();
        else return null;
    }

}
