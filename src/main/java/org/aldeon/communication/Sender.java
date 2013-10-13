package org.aldeon.communication;

/**
 *
 * Used by communication provider to send messages over medium.
 *
 */
public interface Sender <Format> {
    void send(Format formattedMessage);
}
