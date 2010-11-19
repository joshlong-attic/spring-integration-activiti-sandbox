package com.joshlong.activiti.coordinator;

/**
 * priovides constants that both producer and consumers need to be aware of
 *
 * @author Josh Long
 * @since  1.0
 */
public class CoordinatorConstants {

    static final String COORDINATOR_BASE = "activiti_coordinator";

    static public String keyName(String i){ return COORDINATOR_BASE + ":" +i; }

    static final String PROCESS_NAME = keyName( "process_name");

    static final String PROC_ID = keyName("process_id");

    static final String STATE_NAME = keyName("state_id");

    static final String EXECUTION_ID = keyName("execution_id");


}
