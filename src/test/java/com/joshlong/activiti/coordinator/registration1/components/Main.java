package com.joshlong.activiti.coordinator.registration1.components;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

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
		pizzaShop.orderPizza("josh long",  3 , "pepperoni") ;

		System.out.println( "pizza order receipt: " +
				pizzaShop.orderPizzaAndReturnProcessInstance("Josh Long",10, "cheese")) ;
	}
}
