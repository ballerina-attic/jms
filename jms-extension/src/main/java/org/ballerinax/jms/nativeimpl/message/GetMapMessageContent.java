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

package org.ballerinax.jms.nativeimpl.message;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.BlockingNativeCallableUnit;
import org.ballerinalang.jvm.Strand;
import org.ballerinalang.jvm.values.MapValue;
import org.ballerinalang.jvm.values.MapValueImpl;
import org.ballerinalang.jvm.values.ObjectValue;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.ballerinax.jms.JmsConstants;
import org.ballerinax.jms.JmsUtils;
import org.ballerinax.jms.utils.BallerinaAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

/**
 * Get text content of the JMS Message.
 */
@BallerinaFunction(
        orgName = JmsConstants.BALLERINAX, packageName = JmsConstants.JMS_VERSION,
        functionName = "getMapMessageContent",
        receiver = @Receiver(type = TypeKind.OBJECT, structType = JmsConstants.MESSAGE_OBJ_NAME,
                             structPackage = JmsConstants.PROTOCOL_PACKAGE_JMS)
)
public class GetMapMessageContent extends BlockingNativeCallableUnit {

    private static final Logger log = LoggerFactory.getLogger(GetMapMessageContent.class);

    @Override
    public void execute(Context context) {
    }

    public static Object getMapMessageContent(Strand strand, ObjectValue msgObj) {

        Message jmsMessage = JmsUtils.getJMSMessage(msgObj);
        MapValue<String, Object> messageContent = new MapValueImpl<>();

        try {
            if (jmsMessage instanceof MapMessage) {
                MapMessage mapMessage = (MapMessage) jmsMessage;
                Enumeration enumeration = mapMessage.getMapNames();
                while (enumeration.hasMoreElements()) {
                    String key = (String) enumeration.nextElement();
                    Object value = mapMessage.getObject(key);
                    if (value instanceof String || value instanceof Character) {
                        messageContent.put(key, String.valueOf(value));
                    } else if (value instanceof Boolean || value instanceof Integer || value instanceof Long ||
                            value instanceof Short || value instanceof Float || value instanceof Double ||
                            value instanceof byte[]) {
                        messageContent.put(key, value);
                    } else {
                        log.error("Couldn't set invalid data type to map : {}", value.getClass().getSimpleName());
                    }
                }
            } else {
                log.error("JMSMessage is not a Map message. ");
            }
        } catch (JMSException e) {
            return BallerinaAdapter.getError("Error when retrieving JMS message content.", e);
        }

        if (log.isDebugEnabled()) {
            log.debug("Get content from the JMS message");
        }
        return messageContent;
    }

}
