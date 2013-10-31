package org.aldeon.protocol.action;

import org.aldeon.core.Core;
import org.aldeon.events.Callback;
import org.aldeon.model.Message;
import org.aldeon.protocol.Action;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetMessageRequest;
import org.aldeon.protocol.response.MessageFoundResponse;
import org.aldeon.protocol.response.MessageNotFoundResponse;

import java.util.concurrent.Executor;

public class GetMessageAction implements Action<GetMessageRequest> {

    private final Core core;

    public GetMessageAction(Core core) {
        this.core = core;
    }

    @Override
    public void respond(GetMessageRequest request, final Callback<Response> onResponse, Executor executor) {

        /*
            A peer has asked if we have a message with a certain ID.
            We check our database and return an answer as soon as
            the database has the result.
         */

        core.getStorage().getMessageByIdentifier(request.id, new Callback<Message>() {

            /*
                This callback is called when the database completes the search
                for a message with a given id.
             */

            @Override
            public void call(Message val) {

                /*
                    So, did we find the message?
                 */

                if(val == null) {
                    // Nope, message not found.
                    onResponse.call(new MessageNotFoundResponse());
                } else {
                    // Yep, there it is.
                    onResponse.call(new MessageFoundResponse(val));
                }

            }
        }, executor); /*  <---- Someone who called this action gave us an executor so all the
                                hard work should be performed there. We pass this executor on
                                to the database so the onResponse.call() is executed in there.

        */
    }
}
