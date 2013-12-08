package org.aldeon.dht.ring;

import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;

import java.util.Set;

public interface Ring {
    void insert(PeerAddress address);
    void remove(PeerAddress address);
    Set<PeerAddress> getNearest(Identifier identifier, int maxResults);
}
