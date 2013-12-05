package org.aldeon.protocol;


import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.db.Db;
import org.aldeon.db.wrappers.DbCallbackThreadDecorator;
import org.aldeon.db.wrappers.DbWorkThreadDecorator;
import org.aldeon.events.AsyncCallback;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.action.CompareTreesAction;
import org.aldeon.protocol.action.GetClockAction;
import org.aldeon.protocol.action.GetDiffAction;
import org.aldeon.protocol.action.GetMessageAction;
import org.aldeon.protocol.action.GetRelevantPeersAction;
import org.aldeon.protocol.action.IndicateInterestAction;
import org.aldeon.protocol.request.CompareTreesRequest;
import org.aldeon.protocol.request.GetClockRequest;
import org.aldeon.protocol.request.GetDiffRequest;
import org.aldeon.protocol.request.GetMessageRequest;
import org.aldeon.protocol.request.GetRelevantPeersRequest;
import org.aldeon.protocol.request.IndicateInterestRequest;

public class ActionsBasedProtocol implements Protocol {

    private final Action<GetMessageRequest> getMessageAction;
    private final Action<GetRelevantPeersRequest> getPeersInterestedAction;
    private final Action<CompareTreesRequest> compareTreesAction;
    private final Action<IndicateInterestRequest> indicateInterestAction;
    private final Action<GetClockRequest> getClockAction;
    private final Action<GetDiffRequest> getDiffAction;

    public ActionsBasedProtocol() {


        Core core = CoreModule.getInstance();

        Db storage = core.getStorage();
        // Execute requests in a separate thread
        storage = new DbWorkThreadDecorator(storage, core.serverSideExecutor());
        // Execute callbacks in a separate thread
        storage = new DbCallbackThreadDecorator(storage, core.serverSideExecutor());

        this.getMessageAction           = new GetMessageAction(storage);
        this.getPeersInterestedAction   = new GetRelevantPeersAction();
        this.compareTreesAction         = new CompareTreesAction(storage);
        this.indicateInterestAction     = new IndicateInterestAction();
        this.getClockAction             = new GetClockAction(storage);
        this.getDiffAction              = new GetDiffAction(storage);
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
        } else if (request instanceof GetDiffRequest) {
            getDiffAction.respond(peer, (GetDiffRequest) request, onResponse);
        } else {
            throw new IllegalArgumentException("Unknown request type");
        }
    }
}
