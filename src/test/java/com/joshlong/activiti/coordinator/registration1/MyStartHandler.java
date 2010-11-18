package com.joshlong.activiti.coordinator.registration1;

import com.joshlong.activiti.coordinator.annotations.ActivitiHandler;
import com.joshlong.activiti.coordinator.annotations.ActivitiState;

import javax.annotation.PostConstruct;

@ActivitiHandler
public class MyStartHandler {

	@PostConstruct
	public void start () throws Throwable{
		System.out.println( "start()");
	}

	@ActivitiState( "confirm-receipt")
	public void confirm(){
		System.out.println( "confirm()") ;
	}
}
