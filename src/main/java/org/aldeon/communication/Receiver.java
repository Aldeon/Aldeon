package org.aldeon.communication;

/**
 *
 * Used by communication provider to receive messages from medium.
 *
 */
public interface Receiver <Format> {
    void setCommunicationProvider(CommunicationProvider<Format> communicationProvider);
}
