package org.aldeon.communication.task;

import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Request;

/**
 * Asynchronous task related to a Request.
 */
public interface RequestTask<T extends PeerAddress> {

    /**
     * Request object related to this task
     * @return
     */
    Request getRequest();

    /**
     * Address associated with this request
     * @return
     */
    T getAddress();
}