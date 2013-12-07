package org.aldeon.dht;

import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.net.AddressAccepter;
import org.aldeon.net.AddressType;
import org.aldeon.net.PeerAddress;

import java.util.Set;

public interface Dht {

    void registerUncertainAddress(PeerAddress address, Identifier topic);
    void registerAddress(PeerAddress address, Identifier topic);
    void removeAddress(PeerAddress address);

    Set<PeerAddress> getInterested(Identifier topic, int maxResults);
    Set<PeerAddress> getNearest(Identifier topic, int maxResults);

    void addBounty(Identifier topic, Callback<PeerAddress> callback);
    void delBounty(Identifier topic, Callback<PeerAddress> callback);

    AddressType acceptedType();
}
