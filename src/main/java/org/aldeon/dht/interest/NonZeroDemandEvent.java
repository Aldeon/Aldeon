package org.aldeon.dht.interest;

import org.aldeon.events.Event;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.AddressType;

public class NonZeroDemandEvent implements Event {

    private final Identifier topic;
    private final AddressType addressType;

    public NonZeroDemandEvent(Identifier topic, AddressType addressType) {
        this.topic = topic;
        this.addressType = addressType;
    }

    public Identifier getTopic() {
        return topic;
    }

    public AddressType getAddressType() {
        return addressType;
    }
}
