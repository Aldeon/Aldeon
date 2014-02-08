package org.aldeon.dht;

import org.aldeon.dht.closeness.ClosenessTracker;
import org.aldeon.dht.interest.InterestTracker;

public interface Dht {
    InterestTracker interestTracker();
    ClosenessTracker closenessTracker();
}
