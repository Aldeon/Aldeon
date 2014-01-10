package org.aldeon.dht.interest;

import org.aldeon.dht.interest.orders.Order;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.utils.various.Predicate;

import java.util.Set;


public class InterestTrackerFilter implements InterestTracker {

    private final InterestTracker tracker;
    private final Predicate<PeerAddress> filter;

    public InterestTrackerFilter(InterestTracker tracker, Predicate<PeerAddress> filter) {
        this.tracker = tracker;
        this.filter = filter;
    }

    @Override
    public void addAddress(PeerAddress address, Identifier topic) {
        if(filter.check(address)) {
            tracker.addAddress(address, topic);
        }
    }

    @Override
    public void delAddress(PeerAddress address, Identifier topic) {
        tracker.delAddress(address, topic);
    }

    @Override
    public void delAddress(PeerAddress address) {
        tracker.delAddress(address);
    }

    @Override
    public Set<PeerAddress> getInterested(AddressType addressType, Identifier topic, int maxResults) {
        return tracker.getInterested(addressType, topic, maxResults);
    }

    @Override
    public void placeOrder(Order order) {
        tracker.placeOrder(order);
    }

    @Override
    public void revokeOrder(Order order) {
        tracker.revokeOrder(order);
    }

    @Override
    public int getDemand(AddressType addressType, Identifier topic) {
        return tracker.getDemand(addressType, topic);
    }
}
