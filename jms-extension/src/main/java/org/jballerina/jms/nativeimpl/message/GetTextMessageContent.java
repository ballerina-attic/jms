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
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BMap;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.ballerinalang.natives.annotations.ReturnType;
import org.jballerina.jms.AbstractBlockingAction;
import org.jballerina.jms.JmsConstants;
import org.jballerina.jms.JmsUtils;
import org.jballerina.jms.utils.BallerinaAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * Get text content of the JMS Message.
 */
@BallerinaFunction(
        orgName = JmsConstants.JBALLERINA, packageName = JmsConstants.JMS_VERSION,
        functionName = "getTextMessageContent",
        receiver = @Receiver(type = TypeKind.OBJECT, structType = JmsConstants.MESSAGE_OBJ_NAME,
                             structPackage = JmsConstants.PROTOCOL_PACKAGE_JMS),
        returnType = {@ReturnType(type = TypeKind.STRING)},
        isPublic = true
)
public class GetTextMessageContent extends AbstractBlockingAction {

    private static final Logger log = LoggerFactory.getLogger(GetTextMessageContent.class);

    @Override
    public void execute(Context context, CallableUnitCallback callableUnitCallback) {

        @SuppressWarnings(JmsConstants.UNCHECKED)
        BMap<String, BValue> messageStruct  = ((BMap<String, BValue>) context.getRefArgument(0));
        Message jmsMessage = JmsUtils.getJMSMessage(messageStruct);

        String messageContent = null;

        try {
            if (jmsMessage instanceof TextMessage) {
                messageContent = ((TextMessage) jmsMessage).getText();
            } else {
                log.error("JMSMessage is not a Text message. ");
            }
        } catch (JMSException e) {
            BallerinaAdapter.returnError("Error when retrieving JMS message content.", context, e);
        }

        if (log.isDebugEnabled()) {
            log.debug("Get content from the JMS message");
        }

        context.setReturnValues(new BString(messageContent));
    }
}
