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

import org.ballerinalang.jvm.values.ObjectValue;
import org.ballerinax.jms.JmsConstants;
import org.ballerinax.jms.utils.BallerinaAdapter;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * Util class to acknowledge a received message.
 */
public class MessageAcknowledgementHandler {

    private MessageAcknowledgementHandler() {
    }

    public static Object handle(ObjectValue consumerConnectorObject, ObjectValue messageBObject) {
//        SessionConnector sessionConnector = (SessionConnector) consumerConnectorObject.getNativeData(
//                JmsConstants.SESSION_CONNECTOR_OBJECT);
        Message message = (Message) messageBObject.getNativeData(JmsConstants.JMS_MESSAGE_OBJECT);
        try {
//            sessionConnector.handleTransactionBlock(context);
            message.acknowledge();
        } catch (JMSException e) {
           return BallerinaAdapter.getError("Message acknowledgement failed.", e);
        }
        return null;
    }
}
