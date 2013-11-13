package org.aldeon.communication;

import org.aldeon.communication.task.InboundRequestTask;
import org.aldeon.events.Callback;
import org.aldeon.net.PeerAddress;

import java.util.concurrent.Executor;

/**
 * Accepts connections from peers.
 */
public interface Receiver<T extends PeerAddress> extends Service{
    void setCallback(Callback<InboundRequestTask<T>> callback, Executor executor);
}
