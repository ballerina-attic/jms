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

package org.ballerinax.jms.nativeimpl.endpoint.topic.subscriber;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.BlockingNativeCallableUnit;
import org.ballerinalang.jvm.Strand;
import org.ballerinalang.jvm.values.ObjectValue;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.ballerinalang.util.exceptions.BallerinaException;
import org.ballerinax.jms.JmsConstants;
import org.ballerinax.jms.JmsUtils;
import org.ballerinax.jms.nativeimpl.endpoint.common.SessionConnector;
import org.ballerinax.jms.utils.BallerinaAdapter;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

/**
 * Create JMS topic subscriber for a topic subscriber endpoint.
 *
 * @since 0.970
 */
@BallerinaFunction(
        orgName = JmsConstants.BALLERINAX,
        packageName = JmsConstants.JMS_VERSION,
        functionName = "createSubscriber",
        receiver = @Receiver(type = TypeKind.OBJECT, structType = JmsConstants.TOPIC_LISTENER_OBJ_NAME,
                             structPackage = JmsConstants.PROTOCOL_PACKAGE_JMS)
)
public class CreateSubscriber extends BlockingNativeCallableUnit {

    @Override
    public void execute(Context context) {
    }

    public static void createSubscriber(Strand strand, ObjectValue topicListenerObj, ObjectValue sessionObj,
                                        String messageSelector, Object dest) {
        Session session = (Session) sessionObj.getNativeData(JmsConstants.JMS_SESSION);
        String topicPattern = null;
        ObjectValue destinationBObject = null;
        if (dest instanceof String) {
            topicPattern = (String) dest;
        } else {
            destinationBObject = (ObjectValue) dest;
        }
        Destination destinationObject = JmsUtils.getDestination(destinationBObject);

        if (JmsUtils.isNullOrEmptyAfterTrim(topicPattern) && destinationObject == null) {
            throw new BallerinaException("Topic pattern and destination cannot be null at the same time");
        }

        try {
            Destination topic = destinationObject != null ? destinationObject :
                    JmsUtils.getTopic(session, topicPattern);
            MessageConsumer consumer = session.createConsumer(topic, messageSelector);

            ObjectValue consumerConnectorBObject = topicListenerObj.getObjectValue(JmsConstants.CONSUMER_ACTIONS);
            consumerConnectorBObject.addNativeData(JmsConstants.JMS_CONSUMER_OBJECT, consumer);
            consumerConnectorBObject.addNativeData(JmsConstants.SESSION_CONNECTOR_OBJECT,
                                                   new SessionConnector(session));
        } catch (JMSException e) {
            BallerinaAdapter.throwBallerinaException("Error while creating Qeueu consumer", e);
        }

    }

}
