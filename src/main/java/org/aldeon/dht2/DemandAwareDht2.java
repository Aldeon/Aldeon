package org.aldeon.dht2;

import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.utils.various.Provider;

public interface DemandAwareDht2 extends Dht2 {

    void addBounty(Identifier topic, Callback<PeerAddress> callback);
    void delBounty(Identifier topic, Callback<PeerAddress> callback);

    void onDemandChanged(Callback<Identifier> callback);
    Provider<Integer> getDemand(Identifier topic);
}
