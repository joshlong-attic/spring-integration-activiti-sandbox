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
package com.joshlong.activiti.coordinator;

import org.activiti.engine.impl.repository.ProcessDefinitionEntity;
import org.activiti.pvm.activity.ActivityExecution;
import org.activiti.pvm.process.PvmProcessDefinition;
import org.springframework.integration.activiti.gateway.AsyncActivityBehaviorMessagingGateway;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.joshlong.activiti.coordinator.CoordinatorConstants.*;


/**
 * This component extends the {@link org.springframework.integration.activiti.gateway.AsyncActivityBehaviorMessagingGateway}.
 * <p/>
 * The idea is that we want a conventions-over-configuration based approach to dispatching state / execution to other
 * nodes that are using the client side counterpart to this class. This component simply transmits everything that a client will need to know to appropriately dispatch work
 * to consumers.
 *
 * @author Josh Long
 * @see org.springframework.integration.activiti.gateway.AsyncActivityBehaviorMessagingGateway
 * @since 1.0
 */
public class CoordinatorGatewayProducer extends AsyncActivityBehaviorMessagingGateway {


    protected String ACTIVITI_COORDINATOR_BASE_HEADER = "activiti-coordinator".toUpperCase(Locale.ENGLISH);


    /**
     * we need the name of the process definition itself
     *
     * @param pvmProcessDefinition this information exists in the <code>&lt;process.. &gt;</code> element in the BPMN
     *                             process definition.
     * @return the String name of the process definition (ie, 'customer-signup-process')
     */
    protected String discoverProcessName(
            PvmProcessDefinition pvmProcessDefinition) {
        String procName = null;

        if (pvmProcessDefinition instanceof ProcessDefinitionEntity) {
            ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) pvmProcessDefinition;
            procName = processDefinitionEntity.getKey();
        } else {
            String id = pvmProcessDefinition.getId();
            if (StringUtils.hasText(id)) {
                int liOfColon = id.lastIndexOf(":");
                if (liOfColon > -1) {
                    procName = id.substring(0, liOfColon);
                }
            }
        }
        return procName;
    }

    @Override
    protected Map<String, Object> outboundMessageConfigurationHook(
            ActivityExecution activityExecution) throws Exception {
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put((EXECUTION_ID), activityExecution.getId());
        headers.put((STATE_NAME), activityExecution.getActivity().getId());
        headers.put((PROC_ID), activityExecution.getActivity().getProcessDefinition().getId());
        headers.put((PROCESS_NAME), discoverProcessName(activityExecution.getActivity().getProcessDefinition()));
        return headers;
    }
}
