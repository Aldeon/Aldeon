package org.aldeon.protocol;


import org.aldeon.core.Core;
import org.aldeon.events.Callback;
import org.aldeon.protocol.action.GetMessageAction;
import org.aldeon.protocol.request.GetMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

public class ProtocolImpl implements Protocol {

    private static final Logger log = LoggerFactory.getLogger(ProtocolImpl.class);

    private final Core core;
    private final Action<GetMessageRequest> getMessageAction;

    public ProtocolImpl(Core core) {
        this.core = core;

        this.getMessageAction = new GetMessageAction(core);
    }

    @Override
    public void createResponse(Request request, final Callback<Response> onResponse, Executor executor) {

        /* --- we are inside the core.getServerSideExecutor() --- */

        /*
             We have access to the application core

             core.getStorage()
             core.getEventLoop()
             core.getSender(addressType)

             ... and more.

             TODO implement all classes in org.aldeon.communication.converter package

         */

        // Here we (eventually, somehow) generate the response
        if(request instanceof GetMessageRequest) {
            getMessageAction.respond((GetMessageRequest) request, onResponse, executor);
        }
        /*
            elseif, elseif...
         */
        else {
            log.warn("Failed to convert a request into a response.");
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    onResponse.call(null);
                }
            });
        }
    }
}
