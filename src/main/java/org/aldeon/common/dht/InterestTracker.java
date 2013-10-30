package org.aldeon.common.dht;

import org.aldeon.common.model.Identifier;
import org.aldeon.common.net.address.PeerAddress;

import java.util.Collection;

public interface InterestTracker<T extends PeerAddress> {
    void add(T address, Identifier interest);
    Collection<T> getInterestedPeers(Identifier identifier);
}
