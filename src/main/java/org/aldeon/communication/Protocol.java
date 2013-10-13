package org.aldeon.communication;

/**
 *
 * Abstract construct for sending and receiving application messages
 *
 */
public class Protocol {
    private ICommunicationProvider commProvider;

    public Protocol(CommunicationProvider<Object> commProvider) {

    }
    public ProtocolMessage receive(ProtocolMessage protocolMessage) {
        return protocolMessage.onReceive();
    }

    public void send(ProtocolMessage protocolMessage) {
        commProvider.send(protocolMessage);
    }
}
