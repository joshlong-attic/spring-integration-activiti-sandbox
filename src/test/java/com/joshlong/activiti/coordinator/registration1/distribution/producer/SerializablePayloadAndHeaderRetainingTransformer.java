package com.joshlong.activiti.coordinator.registration1.distribution.producer;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Component
public class SerializablePayloadAndHeaderRetainingTransformer {

	@Transformer
	public Message<?> keepOnlySerializablObjects (Message<?> in) throws Throwable {
	//	System.out.println("passing through " +
		//		SerializablePayloadAndHeaderRetainingTransformer.class.getName());

		MessageHeaders headers = in.getHeaders();
		Map<String, Object> serializableHeaders = new HashMap<String, Object>();

		for (String k : headers.keySet()) {
			Object val = headers.get(k);

			if (val instanceof Serializable) {
				serializableHeaders.put(k, val);
			}
		}

		Class clz = in.getPayload().getClass();
		Object id = PropertyUtils.getProperty(in.getPayload(), "id");

		Object payload = Collections.singletonMap(clz.getName(), id); // class will always be non-null 

		return MessageBuilder.withPayload(payload).copyHeaders(serializableHeaders).build();
	}
}
