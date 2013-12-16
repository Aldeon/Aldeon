package org.aldeon.dht.interest;

import org.aldeon.dht.interest.orders.Order;
import org.aldeon.events.EventLoop;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.PeerAddress;

import java.util.Set;

public class InterestTrackerEventDecorator implements InterestTracker {

    private final InterestTracker interestTracker;
    private final EventLoop eventLoop;

    public InterestTrackerEventDecorator(InterestTracker interestTracker, EventLoop eventLoop) {
        this.interestTracker = interestTracker;
        this.eventLoop = eventLoop;
    }

    @Override
    public void addAddress(PeerAddress address, Identifier topic) {
        interestTracker.addAddress(address, topic);
    }

    @Override
    public void delAddress(PeerAddress address, Identifier topic) {
        interestTracker.delAddress(address, topic);
    }

    @Override
    public void delAddress(PeerAddress address) {
        interestTracker.delAddress(address);
    }

    @Override
    public Set<PeerAddress> getInterested(AddressType addressType, Identifier topic, int maxResults) {
        return interestTracker.getInterested(addressType, topic, maxResults);
    }

    @Override
    public void placeOrder(Order order) {
        interestTracker.placeOrder(order);
        if(interestTracker.getDemand(order.addressType(), order.topic()) > 0) {
            eventLoop.notify(new NonZeroDemandEvent(order.topic(), order.addressType()));
        }
    }

    @Override
    public void revokeOrder(Order order) {
        interestTracker.revokeOrder(order);
    }

    @Override
    public int getDemand(AddressType addressType, Identifier topic) {
        return interestTracker.getDemand(addressType, topic);
    }
}
