package org.aldeon.dht.crawler;

import org.aldeon.dht.Dht;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetRelevantPeersRequest;
import org.aldeon.protocol.response.RelevantPeersResponse;
import org.aldeon.sync.tasks.AbstractOutboundTask;

public class GetRelevantPeersTask extends AbstractOutboundTask<GetRelevantPeersRequest> {

    private final Callback<Boolean> onFinished;
    private final Dht dht;

    public GetRelevantPeersTask(PeerAddress peerAddress, Identifier topic, Dht dht, Callback<Boolean> onFinished) {
        super(peerAddress);

        this.dht = dht;
        this.onFinished = onFinished;

        setRequest(new GetRelevantPeersRequest());
        getRequest().target = topic;
    }

    @Override
    public void onSuccess(Response response) {
        if(response instanceof RelevantPeersResponse) {
            onRelevantPeers((RelevantPeersResponse) response);
        } else {
            onFinished.call(false);
        }
    }

    private void onRelevantPeers(RelevantPeersResponse response) {
        for(PeerAddress address: response.closest) {
            dht.closenessTracker().addAddress(address);
        }
        for(PeerAddress address: response.interested) {
            dht.closenessTracker().addAddress(address);
            dht.interestTracker().addAddress(address, getRequest().target);
        }
        onFinished.call(true);
    }

    @Override
    public void onFailure(Throwable cause) {
        onFinished.call(false);
    }
}
