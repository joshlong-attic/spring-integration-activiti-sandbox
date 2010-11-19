package com.joshlong.activiti.coordinator;


import com.joshlong.activiti.coordinator.registry.ActivitiStateHandlerRegistry;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;

/**
 * this component knows what to do to handle coordinating inbound messages that have come from the
 * {@link CoordinatorGatewayProducer}
 *
 * @author  Josh Long
 * @since  1.0
 */
public class CoordinatorGatewayClient implements MessageHandler{
    private volatile ActivitiStateHandlerRegistry registry ;

    public void setRegistry(ActivitiStateHandlerRegistry registry) {
        this.registry = registry;
    }

    public void handleMessage(Message<?> message) throws MessagingException {

    }
}
