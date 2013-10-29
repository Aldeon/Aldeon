package org.aldeon.netty.sender;

import org.aldeon.common.net.address.IpPeerAddress;
import org.aldeon.communication.task.OutboundRequestTask;
import org.aldeon.protocol.Request;

import java.util.concurrent.Executor;

public abstract class RequestListener implements OutboundRequestTask<IpPeerAddress> {

    private Request request;
    private IpPeerAddress address;
    private Executor executor;

    public RequestListener(Executor executor, Request request, IpPeerAddress address) {
        this.executor = executor;
        this.request = request;
        this.address = address;
    }

    @Override
    public IpPeerAddress getAddress() {
        return address;
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public int getTimeoutMillis() {
        return 5000;
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }
}
