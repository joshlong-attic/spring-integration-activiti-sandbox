package com.joshlong.activiti.coordinator.aop.registry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.util.Assert;


/**
 * Holds information about what a given mapping will expect.
 * this information is obtained by introspecting the methods and beans at configuration time and building
 * a profile
 *
 * @author Josh Long
 * @since 1.0
 */
public class ProcessAndStateHandlerMapping {

    private final String processName;
    private final String stateName;

    @Override
    public boolean equals(Object o) {
        if (o instanceof ProcessAndStateHandlerMapping) {
            ProcessAndStateHandlerMapping mapping = (ProcessAndStateHandlerMapping) o;
            return new EqualsBuilder().append(
                    this.processName,
                    mapping.processName).append(
                    this.stateName,
                    mapping.stateName).isEquals();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(StringUtils.defaultString(this.processName))
                .append(this.stateName)
                .toHashCode();
    }

    public ProcessAndStateHandlerMapping(String processName, String stateName) {
        this.processName = StringUtils.isEmpty(processName) ? null : processName;
        this.stateName = StringUtils.isEmpty(stateName) ? null : stateName;
        ;
        Assert.notNull(this.stateName, "You must give a stateName");
    }
}