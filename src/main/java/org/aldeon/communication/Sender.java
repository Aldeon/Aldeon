package org.aldeon.communication;

import org.aldeon.communication.task.OutboundRequestTask;
import org.aldeon.net.AddressAccepter;
import org.aldeon.net.AddressType;
import org.aldeon.net.PeerAddress;

import java.util.Set;

/**
 * Provides a way of sending a request to the remote peer.
 */
public interface Sender extends Service, AddressAccepter {
    /**
     * @param task
     */
    void addTask(OutboundRequestTask task);

}
