package org.aldeon.dht;

import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.PeerAddress;

import java.util.Set;

public class DhtTypeCheckDecorator implements Dht {

    private final Dht dht;

    public DhtTypeCheckDecorator(Dht dht) {
        this.dht = dht;
    }

    @Override
    public void registerUncertainAddress(PeerAddress address, Identifier topic) {
        if(accepts(address)) {
            dht.registerUncertainAddress(address, topic);
        } else throw ex();
    }

    @Override
    public void registerAddress(PeerAddress address, Identifier topic) {
        if(accepts(address)) {
            dht.registerAddress(address, topic);
        } else throw ex();
    }

    @Override
    public void removeAddress(PeerAddress address) {
        if(accepts(address)) {
            dht.removeAddress(address);
        } else throw ex();
    }

    @Override
    public Set<PeerAddress> getInterested(Identifier topic, int maxResults) {
        return dht.getInterested(topic, maxResults);
    }

    @Override
    public Set<PeerAddress> getNearest(Identifier topic, int maxResults) {
        return dht.getNearest(topic, maxResults);
    }

    @Override
    public void addBounty(Identifier topic, Callback<PeerAddress> callback) {
        dht.addBounty(topic, callback);
    }

    @Override
    public void delBounty(Identifier topic, Callback<PeerAddress> callback) {
        dht.delBounty(topic, callback);
    }

    @Override
    public AddressType acceptedType() {
        return dht.acceptedType();
    }

    private RuntimeException ex() {
        return new IllegalArgumentException("Address type does not match the dht requirements");
    }

    private boolean accepts(PeerAddress address) {
        return address.getType().equals(acceptedType());
    }
}
