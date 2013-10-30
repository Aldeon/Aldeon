package org.aldeon.common.communication;

import org.aldeon.common.net.address.PeerAddress;
import org.aldeon.common.communication.task.InboundRequestTask;
import org.aldeon.common.events.Callback;

import java.util.concurrent.Executor;

/**
 * Accepts connections from peers.
 */
public interface Receiver<T extends PeerAddress> extends Service{
    void setCallback(Callback<InboundRequestTask<T>> callback, Executor executor);
}
