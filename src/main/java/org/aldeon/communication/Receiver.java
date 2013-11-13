package org.aldeon.communication;

import org.aldeon.communication.task.InboundRequestTask;
import org.aldeon.events.AsyncCallback;
import org.aldeon.net.PeerAddress;

/**
 * Accepts connections from peers.
 */
public interface Receiver<T extends PeerAddress> extends Service{
    void setCallback(AsyncCallback<InboundRequestTask<T>> callback);
}
