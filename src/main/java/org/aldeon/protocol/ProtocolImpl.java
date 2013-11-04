package org.aldeon.protocol;


import org.aldeon.core.Core;
import org.aldeon.events.Callback;
import org.aldeon.protocol.action.CompareTreesAction;
import org.aldeon.protocol.action.CompareTreesForceAction;
import org.aldeon.protocol.action.GetMessageAction;
import org.aldeon.protocol.action.GetPeersInterestedAction;
import org.aldeon.protocol.request.CompareTreesForceRequest;
import org.aldeon.protocol.request.CompareTreesRequest;
import org.aldeon.protocol.request.GetMessageRequest;
import org.aldeon.protocol.request.GetPeersInterestedRequest;
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

        /*
             We have access to the application core

             core.getStorage()
             core.getEventLoop()
             core.getSender(addressType)

             ... and more.

         */

        /*
            TODO: Implement the conversion mechanism

                - org.aldeon.communication.converter    <---  Request/Response <=> String
                - org.aldeon.utils.json                 <---  Serializers & deserializers for GSON
         */

        // Here we (eventually, somehow) generate the response

        if(request instanceof GetMessageRequest) {
            getMessageAction.respond((GetMessageRequest) request, onResponse, executor);
        } else if (request instanceof GetPeersInterestedRequest) {
            (new GetPeersInterestedAction()).respond((GetPeersInterestedRequest) request, onResponse, executor);
        } else if (request instanceof CompareTreesRequest) {
            (new CompareTreesAction(core)).respond((CompareTreesRequest) request, onResponse, executor);
        } else if (request instanceof CompareTreesForceRequest) {
            (new CompareTreesForceAction()).respond((CompareTreesForceRequest) request, onResponse, executor);
        } else {
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
