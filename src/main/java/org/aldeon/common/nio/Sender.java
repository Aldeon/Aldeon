package org.aldeon.common.nio;

import org.aldeon.common.net.address.PeerAddress;
import org.aldeon.common.nio.task.OutboundRequestTask;

/**
 * Provides a way of sending a request to the remote peer.
 */
public interface Sender<T extends PeerAddress> extends Service {
    /**
     * @param task
     */
    void addTask(OutboundRequestTask<T> task);
}
