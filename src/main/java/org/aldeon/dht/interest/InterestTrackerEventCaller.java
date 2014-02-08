package org.aldeon.dht.interest;

import org.aldeon.dht.interest.orders.Order;
import org.aldeon.events.EventLoop;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.PeerAddress;

import java.util.Set;

public class InterestTrackerEventCaller implements InterestTracker {

    private final EventLoop eventLoop;
    private final InterestTracker tracker;

    public InterestTrackerEventCaller(InterestTracker tracker, EventLoop eventLoop) {
        this.tracker = tracker;
        this.eventLoop = eventLoop;
    }

    @Override
    public void addAddress(PeerAddress address, Identifier topic) {
        tracker.addAddress(address, topic);
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
        eventLoop.notify(new DemandChangedEvent(order.addressType(), order.topic()));
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
