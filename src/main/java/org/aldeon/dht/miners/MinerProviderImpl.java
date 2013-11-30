package org.aldeon.dht.miners;

import org.aldeon.communication.Sender;
import org.aldeon.dht.Dht;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;
import org.aldeon.utils.collections.Pair;
import org.aldeon.utils.collections.Provider;

import java.util.concurrent.Executor;

public class MinerProviderImpl<T extends PeerAddress> implements MinerProvider {

    private final Sender<T> sender;
    private final Class<T> classOfT;
    private final Callback<Pair<T, Identifier>> onFound;
    private final Executor executor;
    private final Dht<T> dht;

    public MinerProviderImpl(Sender<T> sender, Executor executor, Class<T> classOfT, final Dht<T> dht) {
        this.sender = sender;
        this.executor = executor;
        this.classOfT = classOfT;
        this.dht = dht;

        this.onFound = new Callback<Pair<T, Identifier>>() {
            @Override
            public void call(Pair<T, Identifier> val) {
                dht.registerUncertainAddress(val.getP(), val.getQ());
            }
        };
    }

    @Override
    public Miner getMiner(Identifier topic, Provider<Integer> demand) {
        return new MinerImpl<>(sender, executor, topic, classOfT, demand, new TargetFinderByClosestId<>(dht, topic), onFound);
    }
}
