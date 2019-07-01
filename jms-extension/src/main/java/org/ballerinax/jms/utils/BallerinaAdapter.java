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

package org.ballerinax.jms.utils;

import org.ballerinalang.jvm.BallerinaErrors;
import org.ballerinalang.jvm.BallerinaValues;
import org.ballerinalang.jvm.util.exceptions.BallerinaException;
import org.ballerinalang.jvm.values.ErrorValue;
import org.ballerinalang.jvm.values.MapValue;
import org.ballerinax.jms.JmsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;

/**
 * Adapter class use used to bridge the connector native codes and Ballerina API.
 */
public class BallerinaAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(BallerinaAdapter.class);

    private BallerinaAdapter() {
    }

    public static void throwBallerinaException(String message, Throwable throwable) {
        LOGGER.error(message, throwable);
        throw new BallerinaException(message + " " + throwable.getMessage(), throwable);
    }

    private static MapValue<String, Object> createErrorRecord() {
        return BallerinaValues.createRecordValue(JmsConstants.PROTOCOL_PACKAGE_JMS, JmsConstants.JMS_ERROR_RECORD);
    }

    public static ErrorValue getError(String errorMessage, JMSException e) {
        LOGGER.error(errorMessage, e);
        return getError(errorMessage);
    }

    public static ErrorValue getError(String errorMessage) {
        MapValue<String, Object> errorRecord = createErrorRecord();
        errorRecord.put(JmsConstants.ERROR_MESSAGE_FIELD, errorMessage);
        return BallerinaErrors.createError(JmsConstants.JMS_ERROR_CODE, errorRecord);
    }
}
