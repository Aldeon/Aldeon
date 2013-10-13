package org.aldeon.communication;

/**
 *  Interface for communication provider used by Protocol.
 */
interface ICommunicationProvider {
    void send(ProtocolMessage protocolMessage);
    void setProtocol(Protocol protocol);
}
