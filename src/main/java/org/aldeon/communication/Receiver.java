package org.aldeon.communication;

/**
 *
 * Used by communication provider to receive messages from medium.
 *
 */
public interface Receiver <Format, Address> {

    void setCommunicationProvider(CommunicationProvider<Format, Address> communicationProvider);

    void start();

    void stop();

    //void setPort(int port); NOT, this configuration is done inside comm provider module
}
