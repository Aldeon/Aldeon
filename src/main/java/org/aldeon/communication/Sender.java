package org.aldeon.communication;

import org.aldeon.communication.task.OutboundRequestTask;
import org.aldeon.net.AddressAccepter;

/**
 * Provides a way of sending a request to the remote peer.
 */
public interface Sender extends Service, AddressAccepter {
    /**
     * @param task
     */
    void addTask(OutboundRequestTask task);

}
