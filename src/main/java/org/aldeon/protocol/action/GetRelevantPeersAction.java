package org.aldeon.protocol.action;

import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.dht.Dht;
import org.aldeon.events.AsyncCallback;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Action;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetRelevantPeersRequest;
import org.aldeon.protocol.response.RelevantPeersResponse;

import java.util.HashSet;
import java.util.Set;

public class GetRelevantPeersAction implements Action<GetRelevantPeersRequest> {

    private static final int LIMIT = 8;

    @Override
    public void respond(PeerAddress peer, GetRelevantPeersRequest request, AsyncCallback<Response> onResponse) {

        Core core = CoreModule.getInstance();

        Set<PeerAddress> interested = new HashSet<>();
        Set<PeerAddress> nearValues = new HashSet<>();

        Dht dht = core.getDht(peer.getClass());

        nearValues.addAll(dht.getNearest(request.target, LIMIT));
        interested.addAll(dht.getInterested(request.target, LIMIT));

        onResponse.call(new RelevantPeersResponse(interested, nearValues));
    }
}
