package org.aldeon.dht.miners;

import org.aldeon.dht.Dht;
import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;
import org.aldeon.utils.collections.Provider;

import java.util.Set;

/**
 * Determines how the next miner target should be picked
 * @param <T>
 */
public class TargetFinderByClosestId<T extends PeerAddress> implements Provider<T> {

    private final Dht<T> dht;
    private final Identifier topic;

    public TargetFinderByClosestId(Dht<T> dht, Identifier topic) {
        this.dht = dht;
        this.topic = topic;
    }

    @Override
    public T get() {
        Set<T> addresses = dht.getNearest(topic, 1);
        return addresses.isEmpty() ? null : addresses.iterator().next();
    }
}
