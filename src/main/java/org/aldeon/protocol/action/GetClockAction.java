package org.aldeon.protocol.action;

import org.aldeon.db.Db;
import org.aldeon.events.ACB;
import org.aldeon.events.AsyncCallback;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Action;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetClockRequest;
import org.aldeon.protocol.response.ClockResponse;

public class GetClockAction implements Action<GetClockRequest> {

    private final Db storage;

    public GetClockAction(Db storage) {
        this.storage = storage;
    }

    @Override
    public void respond(PeerAddress peer, GetClockRequest request, final AsyncCallback<Response> onResponse) {

        storage.getClock(new ACB<Long>(onResponse.getExecutor()) {
            @Override
            protected void react(Long val) {

                ClockResponse response = new ClockResponse();
                response.clock = val;

                onResponse.call(response);
            }
        });

    }
}
