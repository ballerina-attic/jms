import jballerina/jms;
import ballerina/log;

// This initializes a JMS connection with the provider. This example makes use
// of the ActiveMQ Artemis broker for demonstration while it can be tried with
// other brokers that support JMS.

jms:Connection jmsConnection = new({
        initialContextFactory: 
        "org.apache.activemq.artemis.jndi.ActiveMQInitialContextFactory",
        providerUrl: "tcp://localhost:61616"
    });

// This initializes a JMS session on top of the created connection.
jms:Session jmsSession = new(jmsConnection, {
        acknowledgementMode: "AUTO_ACKNOWLEDGE"
    });

// This initializes a queue sender on top of the the created session.
jms:TopicPublisher topicPublisher = new(jmsSession, topicPattern = "BallerinaTopic");

public function main() {
    // This creates a Text message.
    var msg = jmsSession.createTextMessage("Hello from Ballerina");
    if (msg is jms:Message) {
        // This sends the Ballerina message to the JMS provider.
        var returnVal = topicPublisher->send(msg);
        if (returnVal is error) {
            log:printError("Error occurred while sending message",
                err = returnVal);
        }
    } else {
        log:printError("Error occurred while creating message",
            err = msg);
    }
}
