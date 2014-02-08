package org.aldeon.protocol.action;

import com.google.inject.Inject;
import org.aldeon.core.Core;
import org.aldeon.dht.Dht;
import org.aldeon.events.Callback;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.protocol.Action;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.IndicateInterestRequest;
import org.aldeon.protocol.response.AddressDiscardedResponse;
import org.aldeon.protocol.response.AddressSavedResponse;

public class IndicateInterestAction implements Action<IndicateInterestRequest> {

    private final Core core;

    @Inject
    public IndicateInterestAction(Core core) {
        this.core = core;
    }

    @Override
    public void respond(PeerAddress peer, IndicateInterestRequest request, Callback<Response> onResponse) {

        // Someone wants to be inserted into our dht

        Dht dht = core.getDht();

        if(sourceCredible(peer, request.address)) {
            dht.interestTracker().addAddress(request.address, request.topic);
            dht.closenessTracker().delAddress(request.address);
            onResponse.call(new AddressSavedResponse());

        } else {
            onResponse.call(new AddressDiscardedResponse());
        }
    }

    private boolean sourceCredible(PeerAddress source, PeerAddress suggestedAddress) {

        // TODO: implement the source validation logic

        return true;
    }
}
