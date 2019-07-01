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

package org.ballerinax.jms.nativeimpl.endpoint.topic.durable.subscriber;

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

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;

/**
 * Create JMS topic subscriber for a durable topic subscriber endpoint.
 *
 * @since 0.970
 */
@BallerinaFunction(
        orgName = JmsConstants.BALLERINAX,
        packageName = JmsConstants.JMS_VERSION,
        functionName = "createSubscriber",
        receiver = @Receiver(type = TypeKind.OBJECT,
                             structType = JmsConstants.DURABLE_TOPIC_SUBSCRIBER,
                             structPackage = JmsConstants.PROTOCOL_PACKAGE_JMS)
)
public class CreateSubscriber extends BlockingNativeCallableUnit {

    @Override
    public void execute(Context context) {
    }

    public void execute(Strand strand, ObjectValue topicSubscriberObj, ObjectValue sessionObj, String topicPattern,
                        String consumerId, String messageSelector) {
        Session session = (Session) sessionObj.getNativeData(JmsConstants.JMS_SESSION);
        if (JmsUtils.isNullOrEmptyAfterTrim(consumerId)) {
            throw new BallerinaException("Please provide a durable subscription ID");
        }

        try {
            Topic topic = JmsUtils.getTopic(session, topicPattern);
            MessageConsumer consumer = session.createDurableSubscriber(topic, consumerId, messageSelector, false);
            ObjectValue consumerConnectorBObject = topicSubscriberObj.getObjectValue(JmsConstants.CONSUMER_ACTIONS);
            consumerConnectorBObject.addNativeData(JmsConstants.JMS_CONSUMER_OBJECT, consumer);
            consumerConnectorBObject.addNativeData(JmsConstants.SESSION_CONNECTOR_OBJECT,
                                                   new SessionConnector(session));
        } catch (JMSException e) {
            BallerinaAdapter.throwBallerinaException("Error while creating queue consumer", e);
        }

    }


}
