package com.joshlong.activiti.coordinator.registration1.components;

import com.joshlong.activiti.coordinator.annotations.ActivitiComponent;
import com.joshlong.activiti.coordinator.annotations.ProcessStart;
import com.joshlong.activiti.coordinator.annotations.ProcessVariable;

/**
 * simple service that knows how to invoke business processes on your behalf
 *
 * @author Josh Long
 * @since 1.0
 */
@ActivitiComponent
public class PizzaShop {

	@ProcessStart(processKey = "pizzaCustomerService", returnProcessInstanceId = true)
	public String orderPizzaAndReturnProcessInstance(
			@ProcessVariable("customerName") String cn,
			@ProcessVariable("amount") int amount,
			@ProcessVariable("pizza") String pizza) {
		System.out.println( "orderPizzaAndReturnProcessInstance (" + cn + ", " + amount + "," + pizza +")");
		return null;
	}

	@ProcessStart(processKey = "pizzaCustomerService")
	public void orderPizza( @ProcessVariable("customerName") String cn,
												 @ProcessVariable("amount") int amount ,
												 @ProcessVariable("pizza") String pizza
												 ) {
		System.out.println( "orderPizza (" + cn + ", " + amount + "," + pizza +")");
	}
}
