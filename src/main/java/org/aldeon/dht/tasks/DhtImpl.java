package org.aldeon.dht.tasks;

import com.google.common.collect.Multimap;
import org.aldeon.dht.Bounty;
import org.aldeon.dht.Dht;
import org.aldeon.dht.Ring;
import org.aldeon.dht.RingImpl;
import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;
import org.aldeon.utils.math.ByteBufferArithmetic;

import java.util.Set;

public class DhtImpl<T extends PeerAddress> implements Dht<T> {

    private final Ring<T> ring;
    private final Multimap<Identifier, T> interestedOccupied;
    private final Multimap<Identifier, T> interestedAvailable;

    public DhtImpl() {
        this.ring = new RingImpl<>(new ByteBufferArithmetic());
        interestedOccupied = null;
        interestedAvailable = null;
    }

    @Override
    public void registerAddress(T address, Identifier topic) {
        if(topic == null || topic.isEmpty()) {
            // address not related to any particular topic
        } else {
            // address related to a topic
            interestedAvailable.put(topic, address);
        }
        ring.insert(address);
    }

    @Override
    public void removeAddress(T address) {
        ring.remove(address);

    }

    @Override
    public Set<T> getInterested(Identifier topic, int maxResults) {
        return null;
    }

    @Override
    public Set<T> getNearest(Identifier topic, int maxResults) {
        return ring.getNearest(topic, maxResults);
    }

    @Override
    public void addBounty(Bounty<T> bounty) {

    }

    @Override
    public void delBounty(Bounty<T> bounty) {

    }
}
