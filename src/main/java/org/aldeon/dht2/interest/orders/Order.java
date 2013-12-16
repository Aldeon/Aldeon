package org.aldeon.dht2.interest.orders;

import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.PeerAddress;

public interface Order {

    Identifier topic();
    Callback<PeerAddress> callback();
    AddressType addressType();
}
