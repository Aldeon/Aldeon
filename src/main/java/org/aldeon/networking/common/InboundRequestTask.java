package org.aldeon.networking.common;

import org.aldeon.protocol.Response;

/**
 * Represents a protocol request from a foreign peer.
 *
 * The connection is held open for as long as the sendResponse() or discard() methods are called.
 * Response can be sent at any time, in any thread. All the methods are non-blocking.
 */
public interface InboundRequestTask extends RequestTask {

    /**
     * Sends the response back to peer. The connection is closed and the allocated resources are released.
     * @param response
     */
    void sendResponse(Response response);

    /**
     * Checks if the response was sent.
     * @return
     */
    boolean responseSent();

    /**
     * Closes the connection and releases all the allocated resources without sending any response.
     */
    void discard();
}
