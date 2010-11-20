package com.joshlong.activiti.coordinator.registration1;

import org.apache.activemq.spring.ActiveMQConnectionFactoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SharedConfiguration {

	protected String activeMqUrl = "tcp://127.0.0.1:61616";

	@Bean(name = "cf")
	public ActiveMQConnectionFactoryFactoryBean connectionFactory(){
		ActiveMQConnectionFactoryFactoryBean activeMQConnectionFactoryFactoryBean =
				new ActiveMQConnectionFactoryFactoryBean();
		activeMQConnectionFactoryFactoryBean.setTcpHostAndPort( this.activeMqUrl );
		System.out.println( getClass() + " connectionFactory started") ;
		return activeMQConnectionFactoryFactoryBean ;
	}

}
