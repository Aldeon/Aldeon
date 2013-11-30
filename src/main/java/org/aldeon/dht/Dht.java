package org.aldeon.dht;

import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;

import java.util.Set;

public interface Dht<T extends PeerAddress> {

    void registerUncertainAddress(T address, Identifier topic);
    void registerAddress(T address, Identifier topic);
    void removeAddress(T address);

    Set<T> getInterested(Identifier topic, int maxResults);
    Set<T> getNearest(Identifier topic, int maxResults);

    void addBounty(Bounty<T> bounty);
    void delBounty(Bounty<T> bounty);
}
