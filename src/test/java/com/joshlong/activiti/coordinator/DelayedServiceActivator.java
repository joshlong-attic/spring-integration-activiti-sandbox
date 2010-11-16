package com.joshlong.activiti.coordinator;

import org.springframework.integration.Message;
import org.springframework.integration.activiti.ActivitiConstants;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component 
public class DelayedServiceActivator {

	@ServiceActivator
	public Message<?> sayHello(Message<?> requestComingFromActiviti) throws Throwable {


		System.out.println("entering ServiceActivator:sayHello");
		Map<String,Object> headers = requestComingFromActiviti.getHeaders() ;

		for(String k : headers.keySet())
			System.out.println( String.format( "%s = %s" , k, headers.get( k)));

		Thread.sleep(5 * 1000);
		System.out.println("exiting ServiceActivator:sayHello");


		return MessageBuilder.withPayload(requestComingFromActiviti.getPayload()).
				copyHeadersIfAbsent(requestComingFromActiviti.getHeaders())
				.setHeader( ActivitiConstants.WELL_KNOWN_SPRING_INTEGRATION_HEADER_PREFIX + "test", "1 + 1").
				build();
	}
}
