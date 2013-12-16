package org.aldeon.dht.miner;

import org.aldeon.dht.closeness.ClosenessTracker;
import org.aldeon.dht.interest.InterestTracker;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetRelevantPeersRequest;
import org.aldeon.protocol.response.RelevantPeersResponse;
import org.aldeon.sync.tasks.AbstractOutboundTask;

public class RelevantPeersTask extends AbstractOutboundTask<GetRelevantPeersRequest> {

    private final InterestTracker interestTracker;
    private final ClosenessTracker closenessTracker;
    private final Callback<Boolean> onFinished;

    public RelevantPeersTask(PeerAddress peerAddress, Identifier topic, InterestTracker interestTracker, ClosenessTracker closenessTracker, Callback<Boolean> onFinished) {
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
        } else {
            fail();
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
        success();
    }

    @Override
    public void onFailure(Throwable cause) {
        fail();
    }

    private void success() {
        onFinished.call(true);
    }

    private void fail() {
        interestTracker.delAddress(getAddress());
        closenessTracker.delAddress(getAddress());
        onFinished.call(false);
    }
}
