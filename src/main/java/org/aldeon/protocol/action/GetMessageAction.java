package org.aldeon.protocol.action;

import org.aldeon.db.Db;
import org.aldeon.events.ACB;
import org.aldeon.events.AsyncCallback;
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

    public GetMessageAction(Db storage) {
        this.storage = storage;
    }

    @Override
    public void respond(PeerAddress peer, GetMessageRequest request, final AsyncCallback<Response> onResponse) {

        final Executor e = onResponse.getExecutor();

        /*
            A peer has asked if we have a message with a certain ID.
            We check our database and return an answer as soon as
            the database has the result.
         */

        storage.getMessageById(request.id, new ACB<Message>(e) {

            /*
                This callback is called when the database completes the search
                for a message with a given id.
             */

            @Override
            public void react(Message val) {

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
