// Copyright (c) 2018 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

# Defines the possible values for the destination type in JMS `Destination`.
#
# `queue`: Destination type queue
# `topic`: Destination type topic
public type DestinationType "queue"|"topic";

# Constant for JMS destination type queue
public const QUEUE = "queue";

# Constant for JMS destination type topic
public const TOPIC = "topic";

# Destination object
#
# + destinationName - Name of the destination
# + destinationType - Type of the destination, either queue or topic
public type Destination object {
    public string destinationName;
    public string destinationType;

    // This object construction is package private as it needs to be created using the session
    function __init(string destName, string destType) {
        self.destinationName = destName;
        self.destinationType = destType;
    }
};
