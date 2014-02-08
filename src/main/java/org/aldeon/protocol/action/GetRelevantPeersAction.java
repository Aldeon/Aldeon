package org.aldeon.protocol.action;

import com.google.inject.Inject;
import org.aldeon.core.Core;
import org.aldeon.dht.Dht;
import org.aldeon.events.ACB;
import org.aldeon.events.Callback;
import org.aldeon.model.Message;
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
    public void respond(final PeerAddress peer, GetRelevantPeersRequest request, final Callback<Response> onResponse) {

        final Set<PeerAddress> interested = new HashSet<>();
        final Set<PeerAddress> nearValues = new HashSet<>();

        Dht dht = core.getDht();

        nearValues.addAll(dht.closenessTracker().getNearest(peer.getType(), request.topic, LIMIT));
        interested.addAll(dht.interestTracker().getInterested(peer.getType(), request.topic, LIMIT));

        // If we are interested in a topic, include our address in the list

        core.getStorage().getMessageById(request.topic, new ACB<Message>(core.serverSideExecutor()) {
            @Override
            protected void react(Message message) {

                if(message != null) {
                    interested.add(core.reachableLocalAddress(peer));
                }

                onResponse.call(new RelevantPeersResponse(interested, nearValues));
            }
        });
    }
}
