package org.aldeon.dht;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;

import java.util.Set;

public class InterestTrackerImpl<T extends PeerAddress> implements InterestTracker<T> {

    private SetMultimap<Identifier, T> addresses;

    public InterestTrackerImpl() {
        addresses = HashMultimap.create();
    }

    @Override
    public void addEntry(T address, Identifier interest) {
        addresses.put(interest, address);
    }

    @Override
    public void delEntry(T address, Identifier interest) {
        addresses.remove(interest, address);
    }

    @Override
    public Set<T> getInterestedPeers(Identifier identifier) {
        return addresses.get(identifier);
    }
}
