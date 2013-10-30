package org.aldeon.common.communication;

import org.aldeon.common.net.address.PeerAddress;
import org.aldeon.common.communication.task.OutboundRequestTask;

/**
 * Provides a way of sending a request to the remote peer.
 */
public interface Sender<T extends PeerAddress> extends Service {
    /**
     * @param task
     */
    void addTask(OutboundRequestTask<T> task);
}
