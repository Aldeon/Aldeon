package org.aldeon.networking.common;

import java.util.Set;

/**
 * Provides a way of sending a request to the remote peer.
 */
public interface Sender extends Service {

    void addTask(OutboundRequestTask task);
    Set<AddressType> acceptedTypes();
}
