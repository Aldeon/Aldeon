package org.aldeon.networking.common;

import org.aldeon.events.Callback;
import org.aldeon.core.services.Service;

/**
 * Accepts connections from peers.
 */
public interface Receiver extends Service {
    void setCallback(Callback<InboundRequestTask> callback);
}
