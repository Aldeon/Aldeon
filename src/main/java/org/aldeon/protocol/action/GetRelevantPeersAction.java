package org.aldeon.protocol.action;

import com.google.inject.Inject;
import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.dht.Dht;
import org.aldeon.events.AsyncCallback;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Action;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetRelevantPeersRequest;
import org.aldeon.protocol.response.RelevantPeersResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class GetRelevantPeersAction implements Action<GetRelevantPeersRequest> {

    private static final Logger log = LoggerFactory.getLogger(GetRelevantPeersAction.class);

    private static final int LIMIT = 8;
    private final Core core;

    @Inject
    public GetRelevantPeersAction(Core core) {
        this.core = core;
    }

    @Override
    public void respond(PeerAddress peer, GetRelevantPeersRequest request, AsyncCallback<Response> onResponse) {

        Set<PeerAddress> interested = new HashSet<>();
        Set<PeerAddress> nearValues = new HashSet<>();

        Dht dht = core.getDht(peer.getType());

        if(dht != null) {
            nearValues.addAll(dht.getNearest(request.target, LIMIT));
            interested.addAll(dht.getInterested(request.target, LIMIT));
        } else {
            log.warn("Could not fetch a dht for address " + peer.getClass());
        }

        onResponse.call(new RelevantPeersResponse(interested, nearValues));
    }
}
