package com.joshlong.activiti.coordinator.misc;
/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//package org.activiti.engine.impl.bpmn;

import org.activiti.engine.impl.el.Expression;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.pvm.runtime.ExecutionImpl;
import org.activiti.engine.impl.pvm.runtime.OutgoingExecution;

import java.util.*;


/**
 *
 * todo make the iterator variable survive execution 
 * @author Joram Barrez
 * @since devoxx 2010
 *
 */
public class ForEachActivity implements ActivityBehavior {

  protected Expression collectionVariableName;

  protected Expression iteratorVariable;

  public void execute(ActivityExecution execution) throws Exception {

    Collection collection = (Collection) execution.getVariable((String) collectionVariableName.getValue(execution));

    ActivityImpl activity = (ActivityImpl) execution.getActivity();
    TransitionImpl transition = (TransitionImpl) activity.getOutgoingTransitions().get(0);
    Map<TransitionImpl, Object> transitionVariableMap = new HashMap<TransitionImpl, Object>();

    for (Object object : collection) {
      TransitionImpl newTransition = activity.createOutgoingTransition();
      newTransition.setDestination(transition.getDestination());
      transitionVariableMap.put(newTransition, object);
    }

    takeAll((ExecutionImpl)execution, transitionVariableMap);

  }


  // UGLY!!: just copied takeAll for ExecutionImpl:

  public void takeAll(ExecutionImpl rootExecution ,Map<TransitionImpl, Object> transitionVariableMap) {
    List<PvmTransition> transitions = new ArrayList<PvmTransition>(transitionVariableMap.keySet());

    ExecutionImpl concurrentRoot = ((rootExecution.isConcurrent() && !rootExecution.isScope()) ? rootExecution.getParent() : rootExecution);
    List<ExecutionImpl> concurrentActiveExecutions = new ArrayList<ExecutionImpl>();
    for (ExecutionImpl execution: concurrentRoot.getExecutions()) {
      if (execution.isActive()) {
        concurrentActiveExecutions.add(execution);
      }
    }

    if ( (transitions.size()==1) && (concurrentActiveExecutions.isEmpty()) ) {

      concurrentRoot.setActive(true);
      concurrentRoot.setActivity(rootExecution.getActivity());
      concurrentRoot.setConcurrent(false);
      concurrentRoot.take(transitions.get(0));

    } else {

      List<OutgoingExecution> outgoingExecutions = new ArrayList<OutgoingExecution>();

      // first create the concurrent executions
      while (!transitions.isEmpty()) {
        PvmTransition outgoingTransition = transitions.remove(0);

        ExecutionImpl outgoingExecution = concurrentRoot.createExecution();
        outgoingExecution.setActive(true);
        outgoingExecution.setScope(false);
        outgoingExecution.setConcurrent(true);
        outgoingExecutions.add(new OutgoingExecution(outgoingExecution, outgoingTransition, true));
        outgoingExecution.setVariable((String) iteratorVariable.getValue(rootExecution), transitionVariableMap.get(outgoingTransition));
      }

      // then launch all the concurrent executions
      for (OutgoingExecution outgoingExecution: outgoingExecutions) {
        outgoingExecution.take();
      }
    }
  }


}
