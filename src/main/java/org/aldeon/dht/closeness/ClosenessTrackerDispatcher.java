package org.aldeon.dht.closeness;

import org.aldeon.model.Identifier;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.PeerAddress;

import java.util.Map;
import java.util.Set;

public class ClosenessTrackerDispatcher implements ClosenessTracker {

    private final Map<AddressType, ClosenessTracker> closenessTrackers;

    public ClosenessTrackerDispatcher(Map<AddressType, ClosenessTracker> closenessTrackers) {
        this.closenessTrackers = closenessTrackers;
    }

    private ClosenessTracker tracker(AddressType type) {
        ClosenessTracker tracker = closenessTrackers.get(type);
        if(tracker == null) {
            throw new IllegalArgumentException();
        } else {
            return tracker;
        }
    }

    @Override
    public void addAddress(PeerAddress address) {
        tracker(address.getType()).addAddress(address);
    }

    @Override
    public void delAddress(PeerAddress address) {
        tracker(address.getType()).delAddress(address);
    }

    @Override
    public Set<PeerAddress> getNearest(AddressType addressType, Identifier identifier, int maxResults) {
        return tracker(addressType).getNearest(addressType, identifier, maxResults);
    }
}
