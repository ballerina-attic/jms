/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.ballerinax.jms.nativeimpl.endpoint.common;

import org.ballerinalang.jvm.Strand;
import org.ballerinalang.jvm.values.ObjectValue;
import org.ballerinax.jms.JmsConstants;
import org.ballerinax.jms.JmsMessageListenerImpl;
import org.ballerinax.jms.utils.BallerinaAdapter;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

/**
 * Util class to create and register a message listener.
 *
 * @since 0.970
 */
public class MessageListenerHandler {

    private MessageListenerHandler() {
    }

    public static Object createAndRegister(Strand strand, ObjectValue listenerObj, ObjectValue service) {
        ObjectValue consumerConnector = listenerObj.getObjectValue(JmsConstants.CONSUMER_ACTIONS);
        Object nativeData = consumerConnector.getNativeData(JmsConstants.JMS_CONSUMER_OBJECT);
        if (nativeData instanceof MessageConsumer) {
            MessageListener listener = new JmsMessageListenerImpl(strand.scheduler, service, consumerConnector);
            try {
                ((MessageConsumer) nativeData).setMessageListener(listener);
            } catch (JMSException e) {
                return BallerinaAdapter.getError("Error registering the message listener for service"
                                                         + service.getType().getAnnotationKey(), e);
            }
        }
        return null;
    }
}
