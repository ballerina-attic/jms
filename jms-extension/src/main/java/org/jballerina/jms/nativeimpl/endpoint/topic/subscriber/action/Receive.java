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

package org.jballerina.jms.nativeimpl.endpoint.topic.subscriber.action;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.CallableUnitCallback;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.ballerinalang.natives.annotations.ReturnType;
import org.jballerina.jms.AbstractBlockingAction;
import org.jballerina.jms.JmsConstants;
import org.jballerina.jms.nativeimpl.endpoint.common.ReceiveActionHandler;

/**
 * {@code Receive} is the receive action implementation of the JMS topic subscriber connector.
 */
@BallerinaFunction(orgName = JmsConstants.JBALLERINA,
                   packageName = JmsConstants.JMS_VERSION,
                   functionName = "receive",
                   receiver = @Receiver(type = TypeKind.OBJECT,
                                        structType = JmsConstants.TOPIC_SUBSCRIBER_CALLER_OBJ_NAME,
                                        structPackage = JmsConstants.PROTOCOL_PACKAGE_JMS),
                   args = {
                           @Argument(name = "timeInMilliSeconds",
                                     type = TypeKind.INT)
                   },
                   returnType = {
                           @ReturnType(type = TypeKind.OBJECT,
                                       structPackage = JmsConstants.PROTOCOL_PACKAGE_JMS,
                                       structType = JmsConstants.MESSAGE_OBJ_NAME)
                   },
                   isPublic = true
)
public class Receive extends AbstractBlockingAction {

    @Override
    public void execute(Context context, CallableUnitCallback callback) {
        ReceiveActionHandler.handle(context);
    }
}
