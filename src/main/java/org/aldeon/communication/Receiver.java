package org.aldeon.communication;

import org.aldeon.net.PeerAddress;
import org.aldeon.communication.task.InboundRequestTask;
import org.aldeon.events.Callback;

import java.util.concurrent.Executor;

/**
 * Accepts connections from peers.
 */
public interface Receiver<T extends PeerAddress> extends Service{
    void setCallback(Callback<InboundRequestTask<T>> callback, Executor executor);
}
