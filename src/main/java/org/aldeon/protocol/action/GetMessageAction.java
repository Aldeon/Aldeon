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

        core.getStorage().getMessageByIdentifier(request.id, new Callback<Message>() {
            @Override
            public void call(Message val) {
                if(val == null) {
                    onResponse.call(new MessageNotFoundResponse());

                } else {
                    onResponse.call(new MessageFoundResponse(val));
                }
            }
        }, executor);
    }
}
