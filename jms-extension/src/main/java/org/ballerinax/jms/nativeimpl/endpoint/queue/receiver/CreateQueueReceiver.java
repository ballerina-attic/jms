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

package org.ballerinax.jms.nativeimpl.endpoint.queue.receiver;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.BlockingNativeCallableUnit;
import org.ballerinalang.jvm.Strand;
import org.ballerinalang.jvm.util.exceptions.BallerinaException;
import org.ballerinalang.jvm.values.ObjectValue;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.ballerinax.jms.JmsConstants;
import org.ballerinax.jms.JmsUtils;
import org.ballerinax.jms.nativeimpl.endpoint.common.SessionConnector;
import org.ballerinax.jms.utils.BallerinaAdapter;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

/**
 * Create JMS consumer for a consumer endpoint.
 *
 * @since 0.970
 */
@BallerinaFunction(
        orgName = JmsConstants.BALLERINAX,
        packageName = JmsConstants.JMS_VERSION,
        functionName = "createQueueReceiver",
        receiver = @Receiver(type = TypeKind.OBJECT, structType = JmsConstants.QUEUE_LISTENER,
                             structPackage = JmsConstants.PROTOCOL_PACKAGE_JMS)
)
public class CreateQueueReceiver extends BlockingNativeCallableUnit {
    @Override
    public void execute(Context context) {
    }

    public static void createQueueReceiver(Strand strand, ObjectValue queueListener, ObjectValue sessionBObject,
                                           String messageSelector, Object arg) {
        Session session = (Session) sessionBObject.getNativeData(JmsConstants.JMS_SESSION);
        String queueName = null;
        Destination destinationObject = null;
        if (arg instanceof String) {
            queueName = (String) arg;
        } else {
            ObjectValue destinationBObject = (ObjectValue) arg;
            destinationObject = JmsUtils.getDestination(destinationBObject);
        }

        if (JmsUtils.isNullOrEmptyAfterTrim(queueName) && destinationObject == null) {
            throw new BallerinaException("Queue name and destination cannot be null at the same time");
        }

        try {
            Destination queue = destinationObject != null ? destinationObject : session.createQueue(queueName);
            MessageConsumer consumer = session.createConsumer(queue, messageSelector);
            ObjectValue consumerConnectorBObject = queueListener.getObjectValue(JmsConstants.CONSUMER_ACTIONS);
            consumerConnectorBObject.addNativeData(JmsConstants.JMS_CONSUMER_OBJECT, consumer);
            consumerConnectorBObject.addNativeData(JmsConstants.SESSION_CONNECTOR_OBJECT,
                                                   new SessionConnector(session));
        } catch (JMSException e) {
            BallerinaAdapter.throwBallerinaException("Error while creating queue consumer.", e);
        }
    }
}
