package org.aldeon.dht;

import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;

import java.util.Collection;

public interface InterestTracker<T extends PeerAddress> {
    void add(T address, Identifier interest);
    Collection<T> getInterestedPeers(Identifier identifier);
}
