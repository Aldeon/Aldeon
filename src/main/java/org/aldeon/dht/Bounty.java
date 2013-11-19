package org.aldeon.dht;

import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;

public interface Bounty<T extends PeerAddress> {
    Identifier getIdentifier();
    void onPeerFound(T address);
}
