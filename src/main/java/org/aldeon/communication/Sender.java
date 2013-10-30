package org.aldeon.communication;

import org.aldeon.net.PeerAddress;
import org.aldeon.communication.task.OutboundRequestTask;

/**
 * Provides a way of sending a request to the remote peer.
 */
public interface Sender<T extends PeerAddress> extends Service {
    /**
     * @param task
     */
    void addTask(OutboundRequestTask<T> task);
}
