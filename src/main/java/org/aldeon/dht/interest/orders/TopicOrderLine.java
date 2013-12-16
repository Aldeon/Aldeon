package org.aldeon.dht.interest.orders;

import org.aldeon.networking.common.PeerAddress;

import java.util.Set;

public interface TopicOrderLine {

    void addAddress(PeerAddress address);
    void delAddress(PeerAddress address);

    void addOrder(Order order);
    void delOrder(Order order);

    int getDemand();
    boolean isEmpty();

    Set<PeerAddress> getAddresses(int maxResults);
}
