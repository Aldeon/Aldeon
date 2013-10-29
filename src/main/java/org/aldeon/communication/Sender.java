package org.aldeon.communication;

/**
 *
 * Used by communication provider to send messages over medium.
 * Our application acts as a client, asking recipient for data.
 *
 */
public interface Sender <Format, Address> {

    /**
     * Sends request to remote peer and retrieves response.
     *
     * @param formattedMessage request which will be sent to recipient
     * @param recipientAddress to whom this message shall be sent
     * @return response from recipient
     */
    Format send(Format formattedMessage, Address recipientAddress);
}
