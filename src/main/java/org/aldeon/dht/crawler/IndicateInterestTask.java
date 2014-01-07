package org.aldeon.dht.crawler;

import org.aldeon.model.Identifier;
import org.aldeon.networking.common.OutboundRequestTask;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetRelevantPeersRequest;
import org.aldeon.protocol.request.IndicateInterestRequest;
import org.aldeon.sync.tasks.AbstractOutboundTask;

public class IndicateInterestTask extends AbstractOutboundTask<GetRelevantPeersRequest> {

    private final PeerAddress peer;
    private final Identifier topic;
    private final PeerAddress myself;

    public IndicateInterestTask(Identifier topic, PeerAddress peer, PeerAddress myself) {
        super(null);
        this.topic = topic;
        this.peer = peer;
        this.myself = myself;
    }

    @Override
    public void onSuccess(Response response) {

    }

    @Override
    public void onFailure(Throwable cause) {

    }
}
