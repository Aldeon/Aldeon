package org.aldeon.dht2.interest;

import org.aldeon.dht2.interest.orders.Order;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.PeerAddress;

import java.util.Map;
import java.util.Set;

public class InterestTrackerDispatcher implements InterestTracker {

    private final Map<AddressType, InterestTracker> interestTrackers;

    public InterestTrackerDispatcher(Map<AddressType, InterestTracker> interestTrackers) {
        this.interestTrackers = interestTrackers;
    }

    private InterestTracker tracker(AddressType type) {
        InterestTracker tracker = interestTrackers.get(type);
        if(tracker == null) {
            throw new IllegalArgumentException();
        } else {
            return tracker;
        }
    }

    @Override
    public void addAddress(PeerAddress address, Identifier topic) {
        tracker(address.getType()).addAddress(address, topic);
    }

    @Override
    public void delAddress(PeerAddress address, Identifier topic) {
        tracker(address.getType()).delAddress(address, topic);
    }

    @Override
    public void delAddress(PeerAddress address) {
        tracker(address.getType()).delAddress(address);
    }

    @Override
    public Set<PeerAddress> getInterested(AddressType addressType, Identifier topic, int maxResults) {
        return tracker(addressType).getInterested(addressType, topic, maxResults);
    }

    @Override
    public void placeOrder(Order order) {
        tracker(order.addressType()).placeOrder(order);
    }

    @Override
    public void revokeOrder(Order order) {
        tracker(order.addressType()).revokeOrder(order);
    }

    @Override
    public int getDemand(AddressType addressType, Identifier topic) {
        return tracker(addressType).getDemand(addressType, topic);
    }
}
