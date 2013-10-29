package org.aldeon.communication;

/**
 * Abstract construct for sending and receiving application messages
 */
public class CommunicationHandler {

    private Protocol protocol;

    public ProtocolMessage send(ProtocolMessage protocolMessage) {
        protocol.send(protocolMessage, );
    }

    public ProtocolMessage respond(ProtocolMessage query) {
        protocol.respond(query);
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }
}
