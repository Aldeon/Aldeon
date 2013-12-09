package org.aldeon.protocol.action;

import com.google.inject.Inject;
import org.aldeon.core.Core;
import org.aldeon.db.Db;
import org.aldeon.events.Callback;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.protocol.Action;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetClockRequest;
import org.aldeon.protocol.response.ClockResponse;

public class GetClockAction implements Action<GetClockRequest> {

    private final Db storage;

    @Inject
    public GetClockAction(Core core) {
        this.storage = core.getStorage();
    }

    @Override
    public void respond(PeerAddress peer, GetClockRequest request, final Callback<Response> onResponse) {

        storage.getClock(new Callback<Long>() {
            @Override
            public void call(Long val) {

                ClockResponse response = new ClockResponse();
                response.clock = val;

                onResponse.call(response);
            }
        });

    }
}
