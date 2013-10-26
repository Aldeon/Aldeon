package org.aldeon.common.nio;

import org.aldeon.common.nio.task.InboundRequestTask;
import org.aldeon.utils.various.Callback;

/**
 * Accepts connections from peers.
 */
public interface Receiver extends Service{
    void setCallback(Callback<InboundRequestTask> callback);
}
