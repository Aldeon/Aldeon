package org.aldeon.protocol;


import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.events.AsyncCallback;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.action.CompareTreesAction;
import org.aldeon.protocol.action.GetMessageAction;
import org.aldeon.protocol.action.GetPeersInterestedAction;
import org.aldeon.protocol.request.CompareTreesRequest;
import org.aldeon.protocol.request.GetMessageRequest;
import org.aldeon.protocol.request.GetPeersInterestedRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolImpl implements Protocol {

    private static final Logger log = LoggerFactory.getLogger(ProtocolImpl.class);

    private final Action<GetMessageRequest> getMessageAction;
    private final Action<GetPeersInterestedRequest> getPeersInterestedAction;
    private final Action<CompareTreesRequest> compareTreesAction;

    public ProtocolImpl() {

        Core core = CoreModule.getInstance();

        this.getMessageAction           = new GetMessageAction(core.getStorage());
        this.getPeersInterestedAction   = new GetPeersInterestedAction(core);
        this.compareTreesAction         = new CompareTreesAction(core.getStorage());
    }

    @Override
    public void createResponse(PeerAddress peer, Request request, final AsyncCallback<Response> onResponse) {

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
            getMessageAction.respond(peer, (GetMessageRequest) request, onResponse);
        } else if (request instanceof GetPeersInterestedRequest) {
            getPeersInterestedAction.respond(peer, (GetPeersInterestedRequest) request, onResponse);
        } else if (request instanceof CompareTreesRequest) {
            compareTreesAction.respond(peer, (CompareTreesRequest) request, onResponse);
        } else {
            log.warn("Failed to convert a request into a response.");
            onResponse.call(null);
        }
    }
}
