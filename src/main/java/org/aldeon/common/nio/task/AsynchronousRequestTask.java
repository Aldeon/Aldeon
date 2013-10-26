package org.aldeon.common.nio.task;

import org.aldeon.protocol.Request;
import java.util.concurrent.Executor;

/**
 * Asynchronous task related to a Request.
 */
public interface AsynchronousRequestTask {

    /**
     * Requesr object related to this task.
     * @return
     */
    Request getRequest();

    /**
     * Executor on which this task (any any related subtasks) should be executed.
     * @return
     */
    Executor getExecutor();
}
