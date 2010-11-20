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
package com.joshlong.activiti.coordinator;


/**
 * priovides constants that both producer and consumers need to be aware of
 *
 * @author Josh Long
 * @since 1.0
 */
public class CoordinatorConstants {
	static final String COORDINATOR_BASE = "activiti_coordinator";
	static final String PROCESS_NAME = keyName("process_name");
	static final String PROC_ID = keyName("process_id");
	static final String STATE_NAME = keyName("state_id");
	static final String EXECUTION_ID = keyName("execution_id");

	static public String keyName(String i) {
		return COORDINATOR_BASE + ":" + i;
	}
}
