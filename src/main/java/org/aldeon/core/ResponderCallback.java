package org.aldeon.core;

import org.aldeon.common.communication.task.InboundRequestTask;
import org.aldeon.common.protocol.Protocol;
import org.aldeon.common.protocol.Request;
import org.aldeon.common.protocol.Response;
import org.aldeon.common.events.Callback;

/**
 * Main responder class.
 */
public class ResponderCallback implements Callback<InboundRequestTask> {

    private final Protocol protocol;

    public ResponderCallback(Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    public void call(final InboundRequestTask task) {

        // This is our request from a foreign peer
        Request request = task.getRequest();

        // We ask the protocol for an answer and then send it back
        protocol.createResponse(request, new Callback<Response>() {
            @Override
            public void call(Response response) {
                // Send back the response
                task.sendResponse(response);
            }
        });
    }
}
