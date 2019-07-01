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

package org.ballerinax.jms.nativeimpl.endpoint.session;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.BlockingNativeCallableUnit;
import org.ballerinalang.jvm.BallerinaValues;
import org.ballerinalang.jvm.Strand;
import org.ballerinalang.jvm.values.ObjectValue;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.ballerinax.jms.JmsConstants;
import org.ballerinax.jms.utils.BallerinaAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;

/**
 * Create Text JMS Message.
 */
@BallerinaFunction(orgName = JmsConstants.BALLERINAX, packageName = JmsConstants.JMS_VERSION,
                   functionName = "createQueue",
                   receiver = @Receiver(type = TypeKind.OBJECT, structType = JmsConstants.SESSION_OBJ_NAME,
                                        structPackage = JmsConstants.PROTOCOL_PACKAGE_JMS))
public class CreateQueue extends BlockingNativeCallableUnit {

    public static final Logger LOGGER = LoggerFactory.getLogger(CreateQueue.class);

    @Override
    public void execute(Context context) {
    }

    public Object createQueue(Strand strand, ObjectValue sessionObj, String queueName) {

        Queue jmsDestination;
        Session session = (Session) sessionObj.getNativeData(JmsConstants.JMS_SESSION);
        ObjectValue destObj = BallerinaValues.createObjectValue(JmsConstants.PROTOCOL_PACKAGE_JMS,
                                                                JmsConstants.JMS_DESTINATION_OBJ_NAME);
        try {
            jmsDestination = session.createQueue(queueName);
            destObj.addNativeData(JmsConstants.JMS_DESTINATION_OBJECT, jmsDestination);
            destObj.set(JmsConstants.DESTINATION_NAME, jmsDestination.getQueueName());
            destObj.set(JmsConstants.DESTINATION_TYPE, "queue");
        } catch (JMSException e) {
            return BallerinaAdapter.getError("Failed to create queue destination.", e);
        }
        return destObj;
    }

}
