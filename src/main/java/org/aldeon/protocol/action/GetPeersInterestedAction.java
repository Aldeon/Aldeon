package org.aldeon.protocol.action;

import org.aldeon.core.Core;
import org.aldeon.events.Callback;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Action;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetPeersInterestedRequest;
import org.aldeon.protocol.response.RelevantPeersResponse;

import java.util.HashSet;
import java.util.concurrent.Executor;

/**
 *
 */
public class GetPeersInterestedAction implements Action<GetPeersInterestedRequest> {
    private final Core core;

    public GetPeersInterestedAction(Core core) {
        this.core = core;
    }

    @Override
    public void respond(GetPeersInterestedRequest request, Callback<Response> onResponse, Executor executor) {
        //peers interested = peers who asked about this topic
        //                 + us if we have msgs from this topic
        //peers closest = closest in DHT


        HashSet<PeerAddress> interestedPeers;
        HashSet<PeerAddress> closestPeers;

        interestedPeers = (HashSet<PeerAddress>) core.getInterestTracker()
                            .getInterestedPeers(request.target);
        closestPeers = (HashSet<PeerAddress>) core.getDht()
                            .getNearest(request.target, 64);

        onResponse.call(new RelevantPeersResponse(
                        interestedPeers, closestPeers));
    }
}

