package org.aldeon.sync.tasks;

import org.aldeon.communication.task.OutboundRequestTask;
import org.aldeon.events.AsyncCallback;
import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetClockRequest;
import org.aldeon.protocol.response.ClockResponse;

import java.util.concurrent.Executor;

public class GetClockTask<T extends PeerAddress> implements OutboundRequestTask<T> {

    private final T peer;
    private final GetClockRequest request;
    private final AsyncCallback<Long> onClock;

    public GetClockTask(T peer, Identifier topic, AsyncCallback<Long> onClock) {
        this.peer = peer;
        this.request = new GetClockRequest();
        this.request.topic = topic;
        this.onClock = onClock;
    }

    @Override
    public void onSuccess(Response response) {
        if(response instanceof ClockResponse) {
            onClock.call(((ClockResponse) response).clock);
        } else {
            onClock.call(null);
        }
    }

    @Override
    public void onFailure(Throwable cause) {
        onClock.call(null);
    }

    @Override
    public int getTimeoutMillis() {
        return 5000;
    }

    @Override
    public Executor getExecutor() {
        return onClock.getExecutor();
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public T getAddress() {
        return peer;
    }
}
