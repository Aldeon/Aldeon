package org.aldeon.protocol;


import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.events.AsyncCallback;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.action.CompareTreesAction;
import org.aldeon.protocol.action.GetClockAction;
import org.aldeon.protocol.action.GetMessageAction;
import org.aldeon.protocol.action.GetRelevantPeersAction;
import org.aldeon.protocol.action.IndicateInterestAction;
import org.aldeon.protocol.request.CompareTreesRequest;
import org.aldeon.protocol.request.GetClockRequest;
import org.aldeon.protocol.request.GetMessageRequest;
import org.aldeon.protocol.request.GetRelevantPeersRequest;
import org.aldeon.protocol.request.IndicateInterestRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolImpl implements Protocol {

    private static final Logger log = LoggerFactory.getLogger(ProtocolImpl.class);

    private final Action<GetMessageRequest> getMessageAction;
    private final Action<GetRelevantPeersRequest> getPeersInterestedAction;
    private final Action<CompareTreesRequest> compareTreesAction;
    private final Action<IndicateInterestRequest> indicateInterestAction;
    private final Action<GetClockRequest> getClockAction;

    public ProtocolImpl() {

        Core core = CoreModule.getInstance();

        this.getMessageAction           = new GetMessageAction(core.getStorage());
        this.getPeersInterestedAction   = new GetRelevantPeersAction();
        this.compareTreesAction         = new CompareTreesAction(core.getStorage());
        this.indicateInterestAction     = new IndicateInterestAction();
        this.getClockAction             = new GetClockAction(core.getStorage());
    }

    @Override
    public void createResponse(PeerAddress peer, Request request, final AsyncCallback<Response> onResponse) {


        if(request instanceof GetMessageRequest) {
            getMessageAction.respond(peer, (GetMessageRequest) request, onResponse);
        } else if (request instanceof GetRelevantPeersRequest) {
            getPeersInterestedAction.respond(peer, (GetRelevantPeersRequest) request, onResponse);
        } else if (request instanceof CompareTreesRequest) {
            compareTreesAction.respond(peer, (CompareTreesRequest) request, onResponse);
        } else if (request instanceof IndicateInterestRequest) {
            indicateInterestAction.respond(peer, (IndicateInterestRequest) request, onResponse);
        } else if (request instanceof GetClockRequest) {
            getClockAction.respond(peer, (GetClockRequest) request, onResponse);
        } else {
            throw new IllegalArgumentException("Unknown request type");
        }
    }
}
