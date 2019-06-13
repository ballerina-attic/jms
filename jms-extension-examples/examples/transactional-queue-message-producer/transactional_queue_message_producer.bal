import ballerina/jms;
import ballerina/log;

// Initialize a JMS connection with the provider. This example makes use of the
// ActiveMQ Artemis broker for demonstration while it can be tried with other
// brokers that support JMS.

jms:Connection jmsConnection = new({
        initialContextFactory: 
        "org.apache.activemq.artemis.jndi.ActiveMQInitialContextFactory",
        providerUrl: "tcp://localhost:61616"
    });

// Initialize a JMS session on top of the created connection.
jms:Session jmsSession = new(jmsConnection, {
        acknowledgementMode: "SESSION_TRANSACTED"
    });

// Initialize a queue sender.
jms:QueueSender queueSender = new(jmsSession, queueName = "MyQueue");

public function main() {
    // Message is published within the `transaction` block.
    transaction {
        // Create a text message.
        var msg = jmsSession.createTextMessage("Hello from Ballerina");
        if (msg is jms:Message) {
            // Send the message to the JMS provider.
            var result = queueSender->send(msg);
            if (result is error) {
                log:printError("Error occurred while sending message", err = result);
            }
        } else {
            log:printError("Error occurred while creating message",
                err = msg);
        }
    }
}
