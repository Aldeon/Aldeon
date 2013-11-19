package org.aldeon.protocol.action;

import org.aldeon.core.Core;
import org.aldeon.dht.Ring;
import org.aldeon.events.AsyncCallback;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Action;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetPeersInterestedRequest;
import org.aldeon.protocol.response.RelevantPeersResponse;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class GetPeersInterestedAction implements Action<GetPeersInterestedRequest> {

    private static final int CLOSEST_PEERS_LIMIT = 64;

    private final Core core;

    public GetPeersInterestedAction(Core core) {
        this.core = core;
    }

    @Override
    public void respond(PeerAddress peer, GetPeersInterestedRequest request, AsyncCallback<Response> onResponse) {
        //peers interested = peers who asked about this topic
        //                 + us if we have msgs from this topic
        //peers closest = closest in DHT


        Set<PeerAddress> closestPeers = new HashSet<>();

        Set<Class<? extends PeerAddress>> types = null;

        // Iterate through requested address types
        for(Class<? extends PeerAddress> type: types) {
            Ring ring = core.getDht(type);
            if(ring != null) {
                closestPeers.addAll(ring.getNearest(request.target, CLOSEST_PEERS_LIMIT));
            }
        }


        Set<PeerAddress> interestedPeers = core.getInterestTracker().getInterestedPeers(request.target);

        onResponse.call(new RelevantPeersResponse(interestedPeers, closestPeers));
    }
}

