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

import org.ballerinalang.jvm.BallerinaValues;
import org.ballerinalang.jvm.values.ObjectValue;
import org.ballerinax.jms.JmsConstants;
import org.ballerinax.jms.utils.BallerinaAdapter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;

/**
 * {@code Receive} is the receive action implementation of the JMS Connector.
 */
public class ReceiveActionHandler {

    private ReceiveActionHandler() {
    }

    public static Object handle(ObjectValue connectorBObject, long timeInMilliSeconds) {

        MessageConsumer messageConsumer =
                (MessageConsumer) connectorBObject.getNativeData(JmsConstants.JMS_CONSUMER_OBJECT);
//        SessionConnector sessionConnector =
//                (SessionConnector) connectorBObject.getNativeData(JmsConstants.SESSION_CONNECTOR_OBJECT);

        try {
//            sessionConnector.handleTransactionBlock(context);
            Message message = messageConsumer.receive(timeInMilliSeconds);
            if (message != null) {
                ObjectValue messageBObject = BallerinaValues.createObjectValue(JmsConstants.PROTOCOL_PACKAGE_JMS,
                                                                               JmsConstants.MESSAGE_OBJ_NAME);
                messageBObject.addNativeData(JmsConstants.JMS_MESSAGE_OBJECT, message);
                return messageBObject;
            }
        } catch (JMSException e) {
            return BallerinaAdapter.getError("Message receiving failed.", e);
        }
        return null;
    }
}





































