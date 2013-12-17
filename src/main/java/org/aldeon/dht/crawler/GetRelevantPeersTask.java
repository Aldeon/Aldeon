package org.aldeon.dht.crawler;

import org.aldeon.dht.closeness.ClosenessTracker;
import org.aldeon.dht.interest.InterestTracker;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetRelevantPeersRequest;
import org.aldeon.protocol.response.RelevantPeersResponse;
import org.aldeon.sync.tasks.AbstractOutboundTask;

public class GetRelevantPeersTask extends AbstractOutboundTask<GetRelevantPeersRequest> {

    private final InterestTracker interestTracker;
    private final ClosenessTracker closenessTracker;
    private final Runnable onFinished;

    public GetRelevantPeersTask(PeerAddress peerAddress, Identifier topic, InterestTracker interestTracker, ClosenessTracker closenessTracker, Runnable onFinished) {
        super(peerAddress);

        this.interestTracker = interestTracker;
        this.closenessTracker = closenessTracker;
        this.onFinished = onFinished;

        setRequest(new GetRelevantPeersRequest());
        getRequest().target = topic;
    }

    @Override
    public void onSuccess(Response response) {
        if(response instanceof RelevantPeersResponse) {
            onRelevantPeers((RelevantPeersResponse) response);
        }
    }

    private void onRelevantPeers(RelevantPeersResponse response) {
        for(PeerAddress address: response.closestIds) {
            closenessTracker.addAddress(address);
        }
        for(PeerAddress address: response.interested) {
            closenessTracker.addAddress(address);
            interestTracker.addAddress(address, getRequest().target);
        }
        onFinished.run();
    }

    @Override
    public void onFailure(Throwable cause) {
        onFinished.run();
    }
}
