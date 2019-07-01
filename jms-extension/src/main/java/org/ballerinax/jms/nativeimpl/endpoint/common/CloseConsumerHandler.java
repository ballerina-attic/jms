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

import org.ballerinalang.jvm.values.ObjectValue;
import org.ballerinax.jms.JmsConstants;
import org.ballerinax.jms.utils.BallerinaAdapter;

import java.util.concurrent.CountDownLatch;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;

/**
 * Close the message consumer object.
 */
public class CloseConsumerHandler {

    private CloseConsumerHandler() {
    }

    public static Object handle(ObjectValue listenerObj) {
        if (listenerObj.getNativeData(JmsConstants.JMS_CONSUMER_OBJECT) != null) {
            MessageConsumer consumer = (MessageConsumer) listenerObj.getNativeData(JmsConstants.JMS_CONSUMER_OBJECT);
            try {
                if (consumer != null) {
                    consumer.close();
                }
            } catch (JMSException e) {
                return BallerinaAdapter.getError("Error closing message consumer.", e);
            }
        }
        releaseNonDaemonThreadIfRunning(listenerObj);
        return null;
    }

    private static void releaseNonDaemonThreadIfRunning(ObjectValue listenerObj) {
        CountDownLatch countDownLatch =
                (CountDownLatch) listenerObj.getNativeData(JmsConstants.COUNTDOWN_LATCH);
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }
}
