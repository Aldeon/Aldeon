package org.aldeon.dht2;

import org.aldeon.dht2.closeness.ClosenessTracker;
import org.aldeon.dht2.interest.InterestTracker;

public interface Dht2 {
    InterestTracker interestTracker();
    ClosenessTracker closenessTracker();
}
