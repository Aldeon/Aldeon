package org.aldeon.dht.miners;

import org.aldeon.communication.task.OutboundRequestTask;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.exception.UnexpectedResponseTypeException;
import org.aldeon.protocol.request.GetRelevantPeersRequest;
import org.aldeon.protocol.response.RelevantPeersResponse;

public class ObtainRelevantPeersTask implements OutboundRequestTask {

    private final GetRelevantPeersRequest request;
    private final PeerAddress address;
    private final Callback<RelevantPeersResponse> onResponse;
    private final Callback<Throwable> onThrowable;

    public ObtainRelevantPeersTask(PeerAddress address, Identifier topic, Callback<RelevantPeersResponse> onResponse, Callback<Throwable> onThrowable) {
        this.request = new GetRelevantPeersRequest();
        this.request.target = topic;
        this.address = address;
        this.onResponse = onResponse;
        this.onThrowable = onThrowable;
    }

    @Override
    public void onSuccess(Response response) {
        if(response instanceof RelevantPeersResponse) {
            onResponse.call((RelevantPeersResponse) response);
        } else {
            onThrowable.call(new UnexpectedResponseTypeException());
        }
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
    public Request getRequest() {
        return request;
    }

    @Override
    public PeerAddress getAddress() {
        return address;
    }
}
