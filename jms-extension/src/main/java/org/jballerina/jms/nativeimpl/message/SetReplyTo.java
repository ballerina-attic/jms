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

package org.jballerina.jms.nativeimpl.message;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.CallableUnitCallback;
import org.ballerinalang.connector.api.Struct;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BMap;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.jballerina.jms.AbstractBlockingAction;
import org.jballerina.jms.JmsConstants;
import org.jballerina.jms.utils.BallerinaAdapter;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

/**
 * Set a string property in the JMS Message.
 */
@BallerinaFunction(
        orgName = JmsConstants.JBALLERINA,
        packageName = JmsConstants.JMS_VERSION,
        functionName = "setReplyTo",
        receiver = @Receiver(type = TypeKind.OBJECT,
                             structType = JmsConstants.MESSAGE_OBJ_NAME,
                             structPackage = JmsConstants.PROTOCOL_PACKAGE_JMS),
        args = {@Argument(type = TypeKind.OBJECT, structType = JmsConstants.SESSION_OBJ_NAME,
                          structPackage = JmsConstants.PROTOCOL_PACKAGE_JMS, name = "replyTo")
        },
        isPublic = true
)
public class SetReplyTo extends AbstractBlockingAction {

    @Override
    public void execute(Context context, CallableUnitCallback callableUnitCallback) {

        Struct messageStruct = BallerinaAdapter.getReceiverObject(context);
        Message message = BallerinaAdapter.getNativeObject(messageStruct,
                                                           JmsConstants.JMS_MESSAGE_OBJECT,
                                                           Message.class,
                                                           context);
        @SuppressWarnings(JmsConstants.UNCHECKED)
        BMap<String, BValue> destinationBObject = ((BMap<String, BValue>) context.getRefArgument(1));
        Destination destination = BallerinaAdapter.getNativeObject(destinationBObject,
                                                                   JmsConstants.JMS_DESTINATION_OBJECT,
                                                                   Destination.class,
                                                                   context);
        try {
            message.setJMSReplyTo(destination);
        } catch (JMSException e) {
            BallerinaAdapter.returnError("Error when setting replyTo destination", context, e);
        }
    }
}
