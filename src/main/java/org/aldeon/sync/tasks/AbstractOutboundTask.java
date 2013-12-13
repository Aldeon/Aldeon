package org.aldeon.sync.tasks;

import org.aldeon.networking.common.OutboundRequestTask;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.protocol.Request;

public abstract class AbstractOutboundTask<T extends Request> implements OutboundRequestTask {

    private final PeerAddress peerAddress;
    private T request;

    public AbstractOutboundTask(PeerAddress peerAddress) {
        this.peerAddress = peerAddress;
    }

    protected void setRequest(T request) {
        this.request = request;
    }

    @Override
    public int getTimeoutMillis() {
        return 5000;
    }

    @Override
    public T getRequest() {
        return request;
    }

    @Override
    public PeerAddress getAddress() {
        return peerAddress;
    }
}
