package org.aldeon.dht;

import com.google.common.collect.Multimap;
import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;
import org.aldeon.utils.collections.Provider;
import org.aldeon.utils.math.ByteBufferArithmetic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DhtImpl<T extends PeerAddress> implements Dht<T> {

    private final Ring<T> ring;
    private final Multimap<Identifier, T> interestedOccupied;
    private final Multimap<Identifier, T> interestedAvailable;
    private final Map<Identifier, Miner<T>> miners;
    private final Multimap<Identifier, Bounty<T>> bounties;

    public DhtImpl() {
        this.ring = new RingImpl<>(new ByteBufferArithmetic());
        interestedOccupied = null;
        interestedAvailable = null;
        miners = new HashMap<>();
        bounties = null;
    }

    @Override
    public void registerUncertainAddress(T address, Identifier topic) {
        // TODO: handle uncertain addresses
        registerAddress(address, topic);
    }

    @Override
    public void registerAddress(T address, Identifier topic) {
        if(topic == null || topic.isEmpty()) {
            // address not related to any particular topic
        } else {
            // address related to a topic
            interestedAvailable.put(topic, address);
            onDemandChanged(topic);
        }
        ring.insert(address);
    }

    @Override
    public void removeAddress(T address) {
        ring.remove(address);
        // TODO: remove from multimaps
    }

    @Override
    public Set<T> getInterested(Identifier topic, int maxResults) {
        Set<T> result = new HashSet<>();

        Iterator<T> it = interestedAvailable.get(topic).iterator();

        for(int i=0; i<maxResults; ++i) {
            if(it.hasNext()) {
                result.add(it.next());
            } else {
                break;
            }
        }

        it = interestedOccupied.get(topic).iterator();

        for(int i=result.size();i<maxResults;++i) {
            if(it.hasNext()) {
                result.add(it.next());
            } else {
                break;
            }
        }

        return result;
    }

    @Override
    public Set<T> getNearest(Identifier topic, int maxResults) {
        return ring.getNearest(topic, maxResults);
    }

    @Override
    public void addBounty(Bounty<T> bounty) {
        bounties.put(bounty.getIdentifier(), bounty);
        onDemandChanged(bounty.getIdentifier());
    }

    @Override
    public void delBounty(Bounty<T> bounty) {
        bounties.remove(bounty.getIdentifier(), bounty);
        onDemandChanged(bounty.getIdentifier());
    }

    private void onDemandChanged(Identifier id) {



    }

    private Provider<Integer> getDemand(final Identifier topic) {
        return new Provider<Integer>() {
            @Override
            public Integer get() {
                // TODO: bounty count
                int bounties = 0;
                return Math.max(0, bounties - interestedAvailable.get(topic).size());
            }
        };
    }
}
