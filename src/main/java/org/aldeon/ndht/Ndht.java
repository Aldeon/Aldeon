package org.aldeon.ndht;

import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;

import java.util.Set;

public interface Ndht<T extends PeerAddress> {

    void registerAddress(T address, Identifier topic);
    void registerUncertainAddress(T address, Identifier topic);
    void removeAddress(T address);

    Set<T> getPeers(Identifier topic, int maxResults);
}
