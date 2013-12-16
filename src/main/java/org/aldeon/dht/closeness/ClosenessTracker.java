package org.aldeon.dht.closeness;

import org.aldeon.model.Identifier;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.PeerAddress;

import java.util.Set;

public interface ClosenessTracker {
    void addAddress(PeerAddress address);
    void delAddress(PeerAddress address);
    Set<PeerAddress> getNearest(AddressType addressType, Identifier identifier, int maxResults);
}
