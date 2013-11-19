package org.aldeon.dht;

import org.aldeon.net.PeerAddress;

public interface HeadHunter<T extends PeerAddress> {
    void addBounty(Bounty<T> bounty);
    void delBounty(Bounty<T> bounty);
}
