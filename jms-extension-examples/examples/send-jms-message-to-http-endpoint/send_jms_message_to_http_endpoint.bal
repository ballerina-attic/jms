import ballerina/http;
import jballerina/jms;
import ballerina/log;

// Create a simple queue receiver.  This example makes use of the
// ActiveMQ Artemis broker for demonstration while it can be tried with other
// brokers that support JMS.

listener jms:QueueListener consumerEndpoint = new({
        initialContextFactory: 
        "org.apache.activemq.artemis.jndi.ActiveMQInitialContextFactory",
        providerUrl: "tcp://localhost:61616",
        acknowledgementMode: "AUTO_ACKNOWLEDGE"
    }, queueName = "MyQueue");

// Bind the created JMS consumer to the listener service.
service jmsListener on consumerEndpoint {

    resource function onMessage(jms:QueueReceiverCaller consumer,
                                jms:Message message) {
        var textContent = message.getTextMessageContent();
        if (textContent is string) {
            log:printInfo("Message received from broker. Payload: " +
                    textContent);
            forwardToBackend(untaint textContent);
        } else {
            log:printError("Error while reading message", err = textContent);
        }
    }
}

function forwardToBackend(string textContent) {
    // Create an HTTP client endpoint.
    http:Client clientEP = new("http://localhost:9090/");

    // Forward the received text content of the JMS message to the backend Service
    // using the HTTP client endpoint.
    http:Request req = new;
    req.setPayload(textContent);
    var result = clientEP->post("/backend/jms", req);
    if (result is http:Response) {
        var responseMessage = result.getTextPayload();
        if (responseMessage is string) {
            log:printInfo("Response from backend service: "
                    + responseMessage);
        } else {
            log:printError("Error while reading response",
                            err = responseMessage);
        }
    } else {
        log:printError("Error while sending payload", err = result);
    }
}

// Backend service that receive the forwarded message from the broker.
service backend on new http:Listener(9090) {

    @http:ResourceConfig {
        methods: ["POST"],
        path: "/jms"
    }
    resource function jmsPayloadReceiver(http:Caller caller, http:Request req) {
        http:Response res = new;

        var stringPayload = req.getTextPayload();
        if (stringPayload is string) {
            log:printInfo("Message received from backend service. "
                    + "Payload: " + stringPayload);

            // A util method that can be used to set `string` payload.
            res.setPayload("Message Received.");

            // Sends the response back to the client.
            var result = caller->respond(res);

            if (result is error) {
                log:printError("Error occurred while acknowledging message",
                                err = result);
            }
        } else {
            log:printError("Error while reading payload", err = stringPayload);
        }
    }
}
