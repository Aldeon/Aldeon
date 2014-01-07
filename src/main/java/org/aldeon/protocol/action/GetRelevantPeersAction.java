package org.aldeon.protocol.action;

import com.google.inject.Inject;
import org.aldeon.core.Core;
import org.aldeon.dht.Dht;
import org.aldeon.events.Callback;
import org.aldeon.networking.common.PeerAddress;
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
    public void respond(PeerAddress peer, GetRelevantPeersRequest request, Callback<Response> onResponse) {

        Set<PeerAddress> interested = new HashSet<>();
        Set<PeerAddress> nearValues = new HashSet<>();

        Dht dht = core.getDht();

        nearValues.addAll(dht.closenessTracker().getNearest(peer.getType(), request.topic, LIMIT));
        interested.addAll(dht.interestTracker().getInterested(peer.getType(), request.topic, LIMIT));

        onResponse.call(new RelevantPeersResponse(interested, nearValues));
    }
}
