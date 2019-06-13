import jballerina/jms;
import ballerina/log;

// This initializes a JMS connection with the provider. This example makes use
// of the ActiveMQ Artemis broker for demonstration while it can be tried with
// other brokers that support JMS.

jms:Connection conn = new({
        initialContextFactory: "org.apache.activemq.artemis.jndi.ActiveMQInitialContextFactory",
        providerUrl: "tcp://localhost:61616"
    });

// This initializes a JMS session on top of the created connection.
jms:Session jmsSession = new(conn, {
        // An optional property that defaults to `AUTO_ACKNOWLEDGE`.
        acknowledgementMode: "AUTO_ACKNOWLEDGE"
    });

// This initializes a queue receiver using the created session.
listener jms:QueueListener consumerEndpoint = new(jmsSession, queueName = "MyQueue");

// This binds the created consumer to the listener service.
service jmsListener on consumerEndpoint {

    // This resource is invoked when a message is received.
    resource function onMessage(jms:QueueReceiverCaller consumer,
                                jms:Message message) {
        // Retrieve the text message.
        var messageText = message.getTextMessageContent();
        if (messageText is string) {
            log:printInfo("Message : " + messageText);
        } else {
            log:printError("Error occurred while reading message",
                err = messageText);
        }
    }
}
