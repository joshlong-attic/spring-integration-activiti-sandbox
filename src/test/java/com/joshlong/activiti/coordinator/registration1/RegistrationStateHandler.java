package com.joshlong.activiti.coordinator.registration1;

import com.joshlong.activiti.coordinator.annotations.ActivitiHandler;
import com.joshlong.activiti.coordinator.annotations.ActivitiState;
import com.joshlong.activiti.coordinator.annotations.ProcessId;
import com.joshlong.activiti.coordinator.annotations.ProcessVariable;

import javax.annotation.PostConstruct;

@ActivitiHandler(processName = "customer-fullfillment-process")
public class RegistrationStateHandler {

	@PostConstruct
	public void setup () throws Throwable{
		System.out.println( "start()");
	}

    @ActivitiState( processName = "customer-signup-process", stateName = "start-signup")
    public void start (@ProcessVariable("customerId") long customerId, @ProcessId String procId){
        System.out.println("start() "+ customerId);
    }

	@ActivitiState( "confirm-receipt")
	public void confirm(){
		System.out.println( "confirm()") ;
	}
}
