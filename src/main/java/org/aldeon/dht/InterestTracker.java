package org.aldeon.dht;

import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;

import java.util.Set;

public interface InterestTracker<T extends PeerAddress> {
    void addEntry(T address, Identifier interest);
    void delEntry(T address, Identifier interest);
    Set<T> getInterestedPeers(Identifier identifier);
}
