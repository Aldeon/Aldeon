package org.aldeon.communication;

import org.aldeon.common.net.address.PeerAddress;
import org.aldeon.communication.task.InboundRequestTask;
import org.aldeon.utils.various.Callback;

/**
 * Accepts connections from peers.
 */
public interface Receiver<T extends PeerAddress> extends Service{
    void setCallback(Callback<InboundRequestTask<T>> callback);
}
