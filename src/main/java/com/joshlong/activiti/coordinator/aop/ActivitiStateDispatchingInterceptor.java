package com.joshlong.activiti.coordinator.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.support.channel.ChannelResolver;

/**
 *
 * Simple {@link org.aopalliance.intercept.MethodInterceptor} that wraps a method execution
 * and forwards a message (the 'reply') to the {@link com.joshlong.activiti.coordinator.InboundActivitiCoordinatorDispatcher}
 * which in turn forwards it onto the well known destination which will ultimately connect it to
 * the {@link com.joshlong.activiti.coordinator.OutboundActivitiCoodinatorGateway} instance.
 *
 * This interceptor is what runs the method on your behalf on this - the client  - side.
 *
 *
 * @author Josh Long
 * @since 1.0
 */
public class ActivitiStateDispatchingInterceptor  implements MethodInterceptor {


	private final MessagingTemplate messagingTemplate = new MessagingTemplate();

	//private volatile PublisherMetadataSource metadataSource;

	private final ExpressionParser parser = new SpelExpressionParser(new SpelParserConfiguration(true, true));

	private volatile ChannelResolver channelResolver;

	private final ParameterNameDiscoverer parameterNameDiscoverer =  new LocalVariableTableParameterNameDiscoverer();


	public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println( "invoking the method before");
        Object res = invocation.proceed();
        System.out.println( "invoking the method after");
	    return res;
    }
}
