package org.aldeon.sync.tasks;

import org.aldeon.events.Callback;
import org.aldeon.networking.common.OutboundRequestTask;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetClockRequest;
import org.aldeon.protocol.response.ClockResponse;

public class GetClockTask extends BaseOutboundTask<GetClockRequest> implements OutboundRequestTask {

    private final Callback<Long> onClock;

    public GetClockTask(PeerAddress peer, Identifier topic, Callback<Long> onClock) {
        super(5000, peer);

        setRequest(new GetClockRequest());
        req().topic = topic;

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
}
