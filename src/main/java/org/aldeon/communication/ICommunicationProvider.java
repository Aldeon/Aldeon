package org.aldeon.communication;

/**
 *  Interface for communication provider used by Protocol.
 */
interface ICommunicationProvider<Address> {
    /**
     * Sends protocol message to another peer and retrieves a response
     *
     * @param protocolMessage message to send
     * @return response from remote peer
     */
    ProtocolMessage send(ProtocolMessage protocolMessage, Address recipientAddress);
    void setProtocol(Protocol protocol);
}
