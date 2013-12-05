package org.aldeon.communication;

import org.aldeon.communication.task.InboundRequestTask;
import org.aldeon.events.Callback;

/**
 * Accepts connections from peers.
 */
public interface Receiver extends Service{
    void setCallback(Callback<InboundRequestTask> callback);
}
