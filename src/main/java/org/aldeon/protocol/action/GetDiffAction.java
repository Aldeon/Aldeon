package org.aldeon.protocol.action;

import com.google.inject.Inject;
import org.aldeon.core.Core;
import org.aldeon.db.Db;
import org.aldeon.events.Callback;
import org.aldeon.model.Message;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.protocol.Action;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetDiffRequest;
import org.aldeon.protocol.response.DiffResponse;

import java.util.HashMap;
import java.util.Set;

public class GetDiffAction implements Action<GetDiffRequest> {

    private final Db storage;

    @Inject
    public GetDiffAction(Core core) {
        this.storage = core.getStorage();
    }

    @Override
    public void respond(PeerAddress peer, final GetDiffRequest request, final Callback<Response> onResponse) {

        // 1. Get the current clock value
        // 2. Fetch all the messages inserted after the old clock value (given in request)

        storage.getClock(new Callback<Long>() {
            @Override
            public void call(final Long clock) {

                storage.getMessagesAfterClock(request.topic, request.clock, new Callback<Set<Message>>() {
                    @Override
                    public void call(Set<Message> messages) {

                        DiffResponse response = new DiffResponse();

                        response.ids = new HashMap<>();
                        response.clock = clock;

                        for(Message message: messages) {
                            response.ids.put(message.getIdentifier(), message.getParentMessageIdentifier());
                        }

                        onResponse.call(response);
                    }
                });
            }
        });
    }
}
