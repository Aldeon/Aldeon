package org.aldeon.dht.tasks;

import org.aldeon.communication.task.OutboundRequestTask;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetRelevantPeersRequest;

import java.util.concurrent.Executor;

public class ObtainRelevantPeersTask<T extends PeerAddress> implements OutboundRequestTask<T> {

    private final GetRelevantPeersRequest request;
    private final T address;
    private final Executor executor;
    private final Callback<Response> onResponse;
    private final Callback<Throwable> onThrowable;

    public ObtainRelevantPeersTask(T address, Identifier topic, Executor executor, Callback<Response> onResponse, Callback<Throwable> onThrowable) {
        this.request = new GetRelevantPeersRequest();
        this.request.target = topic;
        this.address = address;
        this.executor = executor;
        this.onResponse = onResponse;
        this.onThrowable = onThrowable;
    }

    @Override
    public void onSuccess(Response response) {
        onResponse.call(response);
    }

    @Override
    public void onFailure(Throwable cause) {
        onThrowable.call(cause);
    }

    @Override
    public int getTimeoutMillis() {
        return 5000;
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public T getAddress() {
        return address;
    }
}
