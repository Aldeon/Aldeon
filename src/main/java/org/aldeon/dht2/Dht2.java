package org.aldeon.dht2;

import org.aldeon.model.Identifier;
import org.aldeon.networking.common.PeerAddress;

import java.util.Set;

public interface Dht2 {

    void registerUncertainAddress(PeerAddress address, Identifier topic);
    void registerAddress(PeerAddress address, Identifier topic);
    void removeAddress(PeerAddress address);

    Set<PeerAddress> getInterested(Identifier topic, int maxResults);
    Set<PeerAddress> getNearest(Identifier topic, int maxResults);

}
