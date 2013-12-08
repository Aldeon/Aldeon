package org.aldeon.communication;

import org.aldeon.communication.task.OutboundRequestTask;
import org.aldeon.networking.common.AddressType;

import java.util.Set;

/**
 * Provides a way of sending a request to the remote peer.
 */
public interface Sender extends Service {

    void addTask(OutboundRequestTask task);
    Set<AddressType> acceptedTypes();
}
