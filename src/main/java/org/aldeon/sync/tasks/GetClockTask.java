package org.aldeon.sync.tasks;

import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetClockRequest;
import org.aldeon.protocol.response.ClockResponse;

public class GetClockTask extends AbstractOutboundTask<GetClockRequest> {
    private final Callback<Long> onFinished;

    public GetClockTask(PeerAddress peerAddress, Identifier topic, Callback<Long> onFinished) {
        super(peerAddress);

        setRequest(new GetClockRequest());
        getRequest().topic = topic;

        this.onFinished = onFinished;
    }

    @Override
    public void onSuccess(Response response) {
        if(response instanceof ClockResponse) {
            onFinished.call(((ClockResponse) response).clock);
        } else {
            onFinished.call(null);
        }
    }

    @Override
    public void onFailure(Throwable cause) {
        onFinished.call(null);
    }
}
