package com.joshlong.activiti.coordinator.registration1.distribution.consumer;

import com.joshlong.activiti.coordinator.annotations.ActivitiComponent;
import com.joshlong.activiti.coordinator.annotations.ActivitiState;
import com.joshlong.activiti.coordinator.annotations.ProcessVariable;

@ActivitiComponent
public class RegistrationConfirmReceiptHandler {
  @ActivitiState("confirm-receipt")
	public void confirm( @ProcessVariable("customerId") Integer customerId) {
		System.out.println("RegistrationConfirmReceiptHandler::confirm-receipt( " + customerId+")");
	}
}
