package com.joshlong.activiti.coordinator.registration1.producer;

import com.sun.deploy.net.MessageHeader;

import org.activiti.engine.impl.bpmn.AbstractBpmnActivity;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

@Component
public class SerializableHeaderRetainingTransformer {
		@Transformer
    public Message<?> keepOnlySerializableHeaders(Message<?> in) {
        MessageHeaders headers = in.getHeaders();
        Map<String, Object> serializableHeaders = new HashMap<String, Object>();
        for (String k : headers.keySet()) {
            Object val = headers.get(k);
            if (val instanceof Serializable) {
                serializableHeaders.put(k, val);
            }
        }
				Object payload = in.getPayload() ;
				if( payload instanceof AbstractBpmnActivity){
					payload = "";
				}
        return MessageBuilder.withPayload( payload ).copyHeaders(serializableHeaders).build();
    }
}
