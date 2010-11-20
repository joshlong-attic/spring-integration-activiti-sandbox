package com.joshlong.activiti.coordinator.registration1.consumer;

import com.joshlong.activiti.coordinator.annotations.ActivitiHandler;
import com.joshlong.activiti.coordinator.annotations.ActivitiState;
import com.joshlong.activiti.coordinator.annotations.ProcessVariable;

@ActivitiHandler
public class RegistrationConfirmReceiptHandler {
  @ActivitiState("confirm-receipt")
	public void confirm( @ProcessVariable("customerId") Integer customerId) {
		System.out.println("RegistrationConfirmReceiptHandler::confirm-receipt( " + customerId+")");
	}
}
