package org.aldeon.dht.interest.orders;

import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.PeerAddress;

public class OrderImpl implements Order {

    private final Identifier topic;
    private final AddressType addressType;
    private final Callback<PeerAddress> onFound;

    public OrderImpl(Identifier topic, AddressType addressType, Callback<PeerAddress> onFound) {
        this.topic = topic;
        this.addressType = addressType;
        this.onFound = onFound;
    }

    @Override
    public Identifier topic() {
        return topic;
    }

    @Override
    public Callback<PeerAddress> callback() {
        return onFound;
    }

    @Override
    public AddressType addressType() {
        return addressType;
    }
}
