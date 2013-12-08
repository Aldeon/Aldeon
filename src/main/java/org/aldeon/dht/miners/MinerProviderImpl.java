package org.aldeon.dht.miners;

import org.aldeon.communication.Sender;
import org.aldeon.dht.Dht;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.utils.collections.Pair;
import org.aldeon.utils.various.Provider;

public class MinerProviderImpl implements MinerProvider {

    private final Sender sender;
    private final Callback<Pair<PeerAddress, Identifier>> onFound;
    private final Dht dht;

    public MinerProviderImpl(Sender sender, final Dht dht) {
        this.sender = sender;
        this.dht = dht;

        this.onFound = new Callback<Pair<PeerAddress, Identifier>>() {
            @Override
            public void call(Pair<PeerAddress, Identifier> val) {
                if(dht.acceptedType() == val.getP().getType()) {
                    dht.registerUncertainAddress(val.getP(), val.getQ());
                }
            }
        };
    }

    @Override
    public Miner getMiner(Identifier topic, Provider<Integer> demand) {
        return new MinerImpl(sender, topic, demand, new TargetFinderByClosestId(dht, topic), onFound);
    }
}
