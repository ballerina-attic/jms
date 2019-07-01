/*
 *  Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.ballerinax.jms.nativeimpl.endpoint.queue.receiver;

import org.ballerinalang.compiler.plugins.SupportedResourceParamTypes;
import org.ballerinalang.util.diagnostic.Diagnostic;
import org.ballerinax.jms.JmsConstants;
import org.ballerinax.jms.JmsServiceCompilerPlugin;
import org.wso2.ballerinalang.compiler.tree.BLangFunction;
import org.wso2.ballerinalang.compiler.tree.BLangSimpleVariable;

import java.util.List;

/**
 * Compiler plugin for validating Jms Listener service.
 *
 * @since 0.995.0
 */
@SupportedResourceParamTypes(
        expectedListenerType = @SupportedResourceParamTypes.Type(packageName = JmsConstants.JMS_VERSION,
                                                                 name = JmsConstants.QUEUE_LISTENER,
                                                                 orgName = JmsConstants.BALLERINAX),
        paramTypes = {@SupportedResourceParamTypes.Type(packageName = JmsConstants.JMS,
                                                        name = JmsConstants.QUEUE_RECEIVER_CALLER_OBJ_NAME,
                                                        orgName = JmsConstants.BALLERINAX)})
public class JmsQueueRecieverServiceCompilerPlugin extends JmsServiceCompilerPlugin {

    @Override
    protected void validateFirstParameter(BLangFunction resource, List<BLangSimpleVariable> paramDetails) {
        if (!JmsConstants.QUEUE_RECEIVER_CALLER_FULL_NAME.equals(paramDetails.get(0).type.toString())) {
            dlog.logDiagnostic(Diagnostic.Kind.ERROR, resource.pos,
                               INVALID_RESOURCE_SIGNATURE_FOR + resource.getName().getValue() +
                                       " resource: The first parameter should be a " +
                                       JmsConstants.QUEUE_RECEIVER_CALLER_FULL_NAME);
        }
    }

}
