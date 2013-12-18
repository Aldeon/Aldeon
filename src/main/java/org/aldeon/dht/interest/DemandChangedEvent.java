package org.aldeon.dht.interest;

import org.aldeon.events.Event;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.AddressType;

public class DemandChangedEvent implements Event {
    private final Identifier topic;
    private final AddressType addressType;

    public DemandChangedEvent(AddressType addressType, Identifier topic) {
        this.addressType = addressType;
        this.topic = topic;
    }

    public Identifier topic() {
        return topic;
    }

    public AddressType addressType() {
        return addressType;
    }
}
