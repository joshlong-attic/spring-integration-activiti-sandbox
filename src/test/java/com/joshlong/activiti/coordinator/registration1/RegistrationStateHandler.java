/*
 * Copyright 2010 the original author or authors
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package com.joshlong.activiti.coordinator.registration1;

import com.joshlong.activiti.coordinator.annotations.ActivitiHandler;
import com.joshlong.activiti.coordinator.annotations.ActivitiState;
import com.joshlong.activiti.coordinator.annotations.ProcessId;
import com.joshlong.activiti.coordinator.annotations.ProcessVariable;

import javax.annotation.PostConstruct;

@ActivitiHandler(processName = "customer-fullfillment-process")
@SuppressWarnings("unused")
public class RegistrationStateHandler {

	@PostConstruct
	public void setup() throws Throwable {
		System.out.println("setup()");
	}

	@ActivitiState(stateName = "start-signup")
	public void start(@ProcessVariable("customerId") long customerId, @ProcessId String procId) {
		System.out.println("start-signup() customerId:" + customerId + " processId: " + procId);
	}

	@ActivitiState("confirm-receipt")
	public void confirm() {
		System.out.println("confirm-receipt()");
	}
}
