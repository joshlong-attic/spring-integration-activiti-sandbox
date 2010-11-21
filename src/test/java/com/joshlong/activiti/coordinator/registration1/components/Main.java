package com.joshlong.activiti.coordinator.registration1.components;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Josh Long
 * @since 1.0
 */
@Component
public class Main {


	public static void main(String [] args) throws Throwable {
		ClassPathXmlApplicationContext classPathXmlApplicationContext =
				new ClassPathXmlApplicationContext("components1.xml") ;
		PizzaShop pizzaShop = classPathXmlApplicationContext.getBean( PizzaShop.class);
		pizzaShop.orderPizza("josh long", 23) ;
	}
}
