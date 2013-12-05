package org.aldeon.communication.task;

import org.aldeon.protocol.Response;

/**
 * Task related to an outgoing request.
 */
public interface OutboundRequestTask extends RequestTask {
    /**
     * Method to be called if the request completes successfully.
     * @param response
     */
    void onSuccess(Response response);

    /**
     * Method to be called if the request fails.
     * @param cause
     */
    void onFailure(Throwable cause);

    /**
     * Indicates when the connection should be closed.
     * @return
     */
    int getTimeoutMillis();
}
