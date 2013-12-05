package org.aldeon.dht.miners;

import org.aldeon.dht.Dht;
import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;
import org.aldeon.utils.various.Provider;

import java.util.Set;

/**
 * Determines how the next miner target should be picked
 */
public class TargetFinderByClosestId implements Provider<PeerAddress> {

    private final Dht dht;
    private final Identifier topic;

    public TargetFinderByClosestId(Dht dht, Identifier topic) {
        this.dht = dht;
        this.topic = topic;
    }

    @Override
    public PeerAddress get() {
        Set<PeerAddress> addresses = dht.getNearest(topic, 1);
        return addresses.isEmpty() ? null : addresses.iterator().next();
    }
}
