package org.aldeon.dht;

import org.aldeon.net.PeerAddress;

public interface Dht<T extends PeerAddress> {
    Ring<T> getRing();
    InterestTracker<T> getInterestTracker();
    HeadHunter<T> getHeadHunter();
}
