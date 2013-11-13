package org.aldeon.dht;

import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;

import java.util.Set;

public interface Dht<T extends PeerAddress> {
    void insert(T address);
    void remove(T address);
    Set<T> getNearest(Identifier identifier, int maxResults);
}
