package com.joshlong.activiti.coordinator;


import com.joshlong.activiti.coordinator.registry.ActivitiStateHandlerRegistration;
import com.joshlong.activiti.coordinator.registry.ActivitiStateHandlerRegistry;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.MessagingException;
import org.springframework.integration.support.MessageBuilder;

import java.util.Collection;

/**
 * this component knows what to do to handle coordinating inbound messages that have come from the
 * {@link CoordinatorGatewayProducer}
 *
 * @author  Josh Long
 * @since  1.0
 */
public class CoordinatorGatewayClient  {
    private volatile ActivitiStateHandlerRegistry registry ;

    public void setRegistry(ActivitiStateHandlerRegistry registry) {
        this.registry = registry;
    }

    public Message<?> processMessage (Message<?> message) throws MessagingException {

        MessageHeaders headers = message.getHeaders();

        String procName = (String)message.getHeaders().get( CoordinatorConstants.PROCESS_NAME);
        String stateName = (String) message.getHeaders().get( CoordinatorConstants.STATE_NAME);
        ActivitiStateHandlerRegistration registration = registry.findRegistrationForProcessAndState( procName, stateName);

        System.out.println( registration.toString()) ;

        return MessageBuilder.withPayload( message.getPayload())
                .copyHeaders(message.getHeaders())
                .build();

    }
}
