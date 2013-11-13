package org.aldeon.core;

import org.aldeon.communication.task.InboundRequestTask;
import org.aldeon.events.ACB;
import org.aldeon.events.Callback;
import org.aldeon.protocol.Protocol;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;

import java.util.concurrent.Executor;

/**
 * Main responder class.
 */
public class ResponderCallback implements Callback<InboundRequestTask> {

    private final Protocol protocol;
    private final Executor executor;

    public ResponderCallback(Protocol protocol, Executor executor) {
        this.protocol = protocol;
        this.executor = executor;
    }

    @Override
    public void call(final InboundRequestTask task) {

        // This is our request from a foreign peer
        Request request = task.getRequest();

        // We ask the protocol for an answer and then send it back
        protocol.createResponse(task.getAddress(), request, new ACB<Response>(executor) {
            @Override
            public void react(Response response) {
                // Send back the response
                task.sendResponse(response);
            }
        });
    }
}
