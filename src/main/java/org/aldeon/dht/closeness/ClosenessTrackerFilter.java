package org.aldeon.dht.closeness;

import org.aldeon.model.Identifier;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.utils.various.Predicate;

import java.util.Set;

public class ClosenessTrackerFilter implements ClosenessTracker {

    private final Predicate<PeerAddress> filter;
    private final ClosenessTracker tracker;

    public ClosenessTrackerFilter(ClosenessTracker tracker, Predicate<PeerAddress> filter) {
        this.tracker = tracker;
        this.filter = filter;
    }

    @Override
    public void addAddress(PeerAddress address) {
        if(filter.check(address)) {
            tracker.addAddress(address);
        }
    }

    @Override
    public void delAddress(PeerAddress address) {
        tracker.delAddress(address);
    }

    @Override
    public Set<PeerAddress> getNearest(AddressType addressType, Identifier identifier, int maxResults) {
        return tracker.getNearest(addressType, identifier, maxResults);
    }
}
