package org.aldeon.communication.task;

import org.aldeon.networking.common.PeerAddress;
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
