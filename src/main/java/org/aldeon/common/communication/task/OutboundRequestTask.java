package org.aldeon.common.communication.task;

import org.aldeon.common.net.address.PeerAddress;
import org.aldeon.common.protocol.Response;

/**
 * Task related to an outgoing request.
 * @param <T>
 */
public interface OutboundRequestTask<T extends PeerAddress> extends AsynchronousRequestTask<T> {
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
