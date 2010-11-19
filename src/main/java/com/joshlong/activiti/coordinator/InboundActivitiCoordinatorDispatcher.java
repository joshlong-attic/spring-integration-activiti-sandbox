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

import org.springframework.integration.MessageChannel;
import org.springframework.integration.endpoint.AbstractEndpoint;
import org.springframework.util.Assert;

/**
 * Consumers will receive the work that matches both the specified <code>process_name</code> <em>and</em> <code>execution_id</code>.
 * <p/>
 * Thus, for the process called <code>customer-signup</code>, and the state
 * called <code>confirm-receipt</code>, an {@link com.joshlong.activiti.coordinator.annotations.ActivitiState}
 * annotations would specify something like:
 * <p/>
 * <code>
 * &at;ActivitiState( processName = "customer-signup", state = "confirm-receipt")
 * public void confirmReceipt( ... )
 * </code>
 * <p/>
 * Alternatively a consumer may simply specify the state, however this means the handler would be enlisted to handle
 * <em>all</em> so-named states. Naturally, this is part of the value of BPM - that your logic be composable - however,
 * it can cause trouble if you truly do have process definition state collissions and didn't intend for them to share the
 * same behavior.
 * <p/>
 * This is a simple endpoint that will spin up and that knows how to react to ibound messages produced from the
 * counterpart on the producer side, the {@link com.joshlong.activiti.coordinator.OutboundActivitiCoodinatorGateway}.
 * <p/>
 * These two endpoints should be used in tandem - as they are coupled by message header keys.
 * <p/>
 * It is conceivable that you could synthesize the header values independant of these endpoints, but not advisable, as the
 * headers are considered internal to this API, and subject to change.
 *
 * @author Josh Long
 * @see com.joshlong.activiti.coordinator.OutboundActivitiCoodinatorGateway
 * @since 1.0
 */
public class InboundActivitiCoordinatorDispatcher extends AbstractEndpoint {

    public void setMessageChannel(MessageChannel messageChannel) {
        this.messageChannel = messageChannel;
    }

    private MessageChannel messageChannel;

    @Override
    protected void onInit() throws Exception {
        Assert.notNull(this.messageChannel, "you must provide a 'messageChannel' instance");
    }

    @Override
    protected void doStart() {

    }

    @Override
    protected void doStop() {

    }

}