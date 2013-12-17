package org.aldeon.dht.closeness;

import org.aldeon.model.Identifier;
import org.aldeon.networking.common.PeerAddress;

import java.util.Set;

public interface Ring {
    void insert(PeerAddress address);
    void remove(PeerAddress address);
    Set<PeerAddress> getNearest(Identifier identifier, int maxResults);
}
