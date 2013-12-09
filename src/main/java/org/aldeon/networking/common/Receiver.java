package org.aldeon.networking.common;

import org.aldeon.events.Callback;

/**
 * Accepts connections from peers.
 */
public interface Receiver extends Service {
    void setCallback(Callback<InboundRequestTask> callback);
}
