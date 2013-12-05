package org.aldeon.protocol.action;

import com.google.inject.Inject;
import org.aldeon.core.Core;
import org.aldeon.db.Db;
import org.aldeon.events.ACB;
import org.aldeon.events.AsyncCallback;
import org.aldeon.model.Message;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Action;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetDiffRequest;
import org.aldeon.protocol.response.DiffResponse;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Executor;

public class GetDiffAction implements Action<GetDiffRequest> {

    private final Db storage;

    @Inject
    public GetDiffAction(Core core) {
        this.storage = core.getStorage();
    }

    @Override
    public void respond(PeerAddress peer, final GetDiffRequest request, final AsyncCallback<Response> onResponse) {

        // 1. Get the current clock value
        // 2. Fetch all the messages inserted after the old clock value (given in request)

        final Executor executor = onResponse.getExecutor();

        storage.getClock(new ACB<Long>(executor) {
            @Override
            protected void react(final Long clock) {

                storage.getMessagesAfterClock(request.topic, request.clock, new ACB<Set<Message>>(executor) {
                    @Override
                    protected void react(Set<Message> messages) {

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
