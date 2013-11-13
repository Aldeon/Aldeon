package org.aldeon.communication.task;

import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Request;

import java.util.concurrent.Executor;

/**
 * Asynchronous task related to a Request.
 */
public interface AsynchronousRequestTask<T extends PeerAddress> {

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

    /**
     * Executor on which this task (any any related subtasks) should be executed
     * @return
     */
    Executor getExecutor();
}
