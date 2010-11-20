package com.joshlong.activiti.coordinator;

import com.joshlong.activiti.coordinator.registry.ActivitiStateHandlerRegistration;
import com.joshlong.activiti.coordinator.registry.ActivitiStateHandlerRegistry;

import org.springframework.beans.BeansException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.MessagingException;
import org.springframework.integration.support.MessageBuilder;

import java.lang.reflect.Method;

import java.util.*;


/**
 * this component knows what to do to handle coordinating inbound messages that have come from the
 * {@link CoordinatorGatewayProducer}
 *
 * @author Josh Long
 * @since 1.0
 */
public class CoordinatorGatewayClient implements ApplicationContextAware {
    private volatile ActivitiStateHandlerRegistry registry;
    private ApplicationContext applicationContext;

    public void setRegistry(ActivitiStateHandlerRegistry registry) {
        this.registry = registry;
    }

    public Message<?> processMessage(Message<?> message)
        throws MessagingException {
        try {
            MessageHeaders headers = message.getHeaders();

            String procName = (String) headers.get(CoordinatorConstants.PROCESS_NAME);

            String stateName = (String) headers.get(CoordinatorConstants.STATE_NAME);

            ActivitiStateHandlerRegistration registration = registry.findRegistrationForProcessAndState(procName,
                    stateName);

            Object bean = applicationContext.getBean(registration.getBeanName());

            System.out.println("---------------------");
            System.out.println("handling " + procName + ":" + stateName);
            System.out.println(registration.toString());

            System.out.println("---------------------");

            Method method = registration.getHandlerMethod();
					int size = method.getParameterTypes().length;
					ArrayList<Object> argsList = new ArrayList<Object>(  );



            // todo support proc var map
            //if(registration.requiresProcessVariablesMap())
            //				argsList.add( registration.getProcessVariablesIndex(),  );
            Map<Integer, String> processVariablesMap = registration.getProcessVariablesExpected();
					// already has which indexes get which process variables

					Map<Integer,Object> variables = new HashMap<Integer,Object> ();
					for( Integer i : processVariablesMap.keySet())
							variables.put(i, headers.get( processVariablesMap.get(i)));


					if (registration.requiresProcessId()) {
							variables.put(  registration.getProcessIdIndex(), headers.get(CoordinatorConstants.PROC_ID)) ;
					}

					System.out.println( variables.toString() ) ;

					List<Integer> indices = new ArrayList<Integer>( variables.keySet() ) ;
					Collections.sort( indices   );

					argsList.clear() ;

					for( Integer idx : indices)
					argsList.add( variables.get(idx)) ;


            Object[] args =  argsList.toArray(new Object[    argsList.size() ]);
            Object result = args.length == 0 ?method.invoke(bean) : method.invoke(bean, args);

            MessageBuilder builder = MessageBuilder.withPayload(message.getPayload())
                                                   .copyHeaders(message.getHeaders());

            if (result instanceof Map) {
                /// todo on the return trip we can update process variables so\
            }

            return builder.build();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        this.applicationContext = applicationContext;
    }
}
