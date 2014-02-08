package org.aldeon.dht.interest;

import org.aldeon.dht.interest.orders.Order;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.PeerAddress;

import java.util.Set;

public interface InterestTracker {
    public void addAddress(PeerAddress address, Identifier topic);
    public void delAddress(PeerAddress address, Identifier topic);
    public void delAddress(PeerAddress address);

    public Set<PeerAddress> getInterested(AddressType addressType, Identifier topic, int maxResults);

    public void placeOrder(Order order);
    public void revokeOrder(Order order);

    int getDemand(AddressType addressType, Identifier topic);
}
