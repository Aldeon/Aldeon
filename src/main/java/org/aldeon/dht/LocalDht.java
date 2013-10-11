package org.aldeon.dht;

import org.aldeon.common.model.Identifier;
import org.aldeon.common.net.PeerAddress;

import java.util.Set;

public interface LocalDht<T extends PeerAddress> {
    void insert(T address);
    void remove(T address);
    Set<T> getNearest(Identifier identifier, int maxResults);
}
