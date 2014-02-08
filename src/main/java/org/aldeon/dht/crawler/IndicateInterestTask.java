package org.aldeon.dht.crawler;

import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.OutboundRequestTask;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetRelevantPeersRequest;
import org.aldeon.protocol.request.IndicateInterestRequest;
import org.aldeon.protocol.response.AddressSavedResponse;
import org.aldeon.sync.tasks.AbstractOutboundTask;

public class IndicateInterestTask extends AbstractOutboundTask<IndicateInterestRequest> {

    private final Callback<Boolean> onFinished;

    public IndicateInterestTask(Identifier topic, PeerAddress peer, PeerAddress myself, Callback<Boolean> onFinished) {
        super(peer);
        setRequest(new IndicateInterestRequest());
        getRequest().topic = topic;
        getRequest().address = myself;
        this.onFinished = onFinished;
    }

    @Override
    public void onSuccess(Response response) {
        onFinished.call(response instanceof AddressSavedResponse);
    }

    @Override
    public void onFailure(Throwable cause) {
        onFinished.call(false);
    }
}
