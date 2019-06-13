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

package org.jballerina.jms.nativeimpl.endpoint.common;

import org.ballerinalang.bre.Context;
import org.ballerinalang.connector.api.BLangConnectorSPIUtil;
import org.ballerinalang.connector.api.Struct;
import org.ballerinalang.model.values.BMap;
import org.ballerinalang.model.values.BValue;
import org.jballerina.jms.JmsConstants;
import org.jballerina.jms.utils.BallerinaAdapter;

import java.util.Objects;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;

/**
 * {@code Receive} is the receive action implementation of the JMS Connector.
 */
public class ReceiveActionHandler {

    private ReceiveActionHandler() {
    }

    public static void handle(Context context) {

        Struct connectorBObject = BallerinaAdapter.getReceiverObject(context);
        MessageConsumer messageConsumer = BallerinaAdapter.
                getNativeObject(connectorBObject, JmsConstants.JMS_CONSUMER_OBJECT, MessageConsumer.class, context);
        SessionConnector sessionConnector = BallerinaAdapter.
                getNativeObject(connectorBObject, JmsConstants.SESSION_CONNECTOR_OBJECT, SessionConnector.class,
                                context);
        long timeInMilliSeconds = context.getIntArgument(0);

        try {
            sessionConnector.handleTransactionBlock(context);
            Message message = messageConsumer.receive(timeInMilliSeconds);
            if (Objects.nonNull(message)) {
                BMap<String, BValue> messageBObject = BLangConnectorSPIUtil.
                        createBStruct(context, JmsConstants.PROTOCOL_PACKAGE_JMS,
                                      JmsConstants.MESSAGE_OBJ_NAME);
                messageBObject.addNativeData(JmsConstants.JMS_MESSAGE_OBJECT, message);
                context.setReturnValues(messageBObject);
            } else {
                context.setReturnValues();
            }
        } catch (JMSException e) {
            BallerinaAdapter.returnError("Message receiving failed.", context, e);
        }
    }
}





































