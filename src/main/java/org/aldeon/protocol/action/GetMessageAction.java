package org.aldeon.protocol.action;

import com.google.inject.Inject;
import org.aldeon.core.Core;
import org.aldeon.db.Db;
import org.aldeon.events.ACB;
import org.aldeon.events.AsyncCallback;
import org.aldeon.events.Callback;
import org.aldeon.model.Message;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Action;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetMessageRequest;
import org.aldeon.protocol.response.MessageFoundResponse;
import org.aldeon.protocol.response.MessageNotFoundResponse;

import java.util.concurrent.Executor;

public class GetMessageAction implements Action<GetMessageRequest> {

    private final Db storage;

    @Inject
    public GetMessageAction(Core core) {
        this.storage = core.getStorage();
    }

    @Override
    public void respond(PeerAddress peer, GetMessageRequest request, final Callback<Response> onResponse) {

        /*
            A peer has asked if we have a message with a certain ID.
            We check our database and return an answer as soon as
            the database has the result.
         */

        storage.getMessageById(request.id, new Callback<Message>() {

            /*
                This callback is called when the database completes the search
                for a message with a given id.
             */

            @Override
            public void call(Message val) {

                /*
                    So, did we find the message?
                 */

                if (val == null) {
                    // Nope, message not found.
                    onResponse.call(new MessageNotFoundResponse());
                } else {
                    // Yep, there it is.
                    onResponse.call(new MessageFoundResponse(val));
                }

            }
        });           /*  <---- Someone who called this action gave us an executor so all the
                                hard work should be performed there. We pass this executor on
                                to the database so the onResponse.call() is executed in there.
                       */
    }
}
