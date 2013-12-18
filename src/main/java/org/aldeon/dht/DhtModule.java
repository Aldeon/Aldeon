package org.aldeon.dht;

import org.aldeon.dht.closeness.ClosenessTracker;
import org.aldeon.dht.closeness.ClosenessTrackerDispatcher;
import org.aldeon.dht.closeness.RingBasedClosenessTracker;
import org.aldeon.dht.closeness.RingImpl;
import org.aldeon.dht.interest.AddressTypeIgnoringInterestTracker;
import org.aldeon.dht.interest.InterestTracker;
import org.aldeon.dht.interest.InterestTrackerDispatcher;
import org.aldeon.dht.interest.InterestTrackerEventCaller;
import org.aldeon.events.EventLoop;
import org.aldeon.networking.common.AddressType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DhtModule {

    public static Dht create(Set<AddressType> acceptedTypes, EventLoop eventLoop) {

        // 1. Allocate appropriate structures

        Map<AddressType, ClosenessTracker> closenessTrackerMap = new HashMap<>();
        Map<AddressType, InterestTracker> interestTrackerMap = new HashMap<>();

        for(AddressType type: acceptedTypes) {
            closenessTrackerMap.put(type, new RingBasedClosenessTracker(new RingImpl()));
            interestTrackerMap.put(type, new AddressTypeIgnoringInterestTracker());
        }

        // 2. Create AddressType-based dispatchers

        ClosenessTracker closenessTracker = new ClosenessTrackerDispatcher(closenessTrackerMap);
        InterestTracker interestTracker = new InterestTrackerDispatcher(interestTrackerMap);

        // 3. Apply decorators

        interestTracker = new InterestTrackerEventCaller(interestTracker, eventLoop);

        // 4. Return dht object

        final InterestTracker it = interestTracker;
        final ClosenessTracker ct = closenessTracker;

        return new Dht() {
            @Override
            public InterestTracker interestTracker() {
                return it;
            }

            @Override
            public ClosenessTracker closenessTracker() {
                return ct;
            }
        };
    }
}
