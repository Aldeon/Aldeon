package org.aldeon.sync.tasks;

import org.aldeon.communication.task.OutboundRequestTask;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.protocol.Request;

public abstract class BaseOutboundTask<R extends Request> implements OutboundRequestTask {

    private R request;
    private final int timeout;
    private final PeerAddress address;

    public BaseOutboundTask(int timeout, PeerAddress address) {
        this.timeout = timeout;
        this.address = address;
    }

    protected void setRequest(R request) {
        this.request = request;
    }

    protected R req() {
        return request;
    }

    @Override
    public int getTimeoutMillis() {
        return timeout;
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public PeerAddress getAddress() {
        return address;
    }
}
