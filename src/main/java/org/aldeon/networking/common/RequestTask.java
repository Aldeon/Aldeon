package org.aldeon.networking.common;

import org.aldeon.protocol.Request;

/**
 * Asynchronous task related to a Request.
 */
public interface RequestTask {

    /**
     * Request object related to this task
     * @return
     */
    Request getRequest();

    /**
     * Address associated with this request
     * @return
     */
    PeerAddress getAddress();
}
