package org.aldeon.dht;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import org.aldeon.common.model.Identifier;
import org.aldeon.common.net.address.PeerAddress;

import java.util.Collection;

public class InterestTrackerImpl<T extends PeerAddress> implements InterestTracker<T> {

    private SetMultimap<Identifier, T> addresses;

    public InterestTrackerImpl() {
        addresses = HashMultimap.create();
    }

    @Override
    public void add(T address, Identifier interest) {
        addresses.put(interest, address);
    }

    @Override
    public Collection<T> getInterestedPeers(Identifier identifier) {
        return addresses.get(identifier);
    }
}
