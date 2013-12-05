package org.aldeon.dht;

import com.google.inject.Inject;
import org.aldeon.dht.miners.DemandWatcher;
import org.aldeon.dht.ring.Ring;
import org.aldeon.dht.slots.AddressAllocator;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.net.AddressType;
import org.aldeon.net.PeerAddress;
import org.aldeon.utils.various.Provider;

import java.util.HashSet;
import java.util.Set;

public class RingBasedDht implements Dht, DemandWatcher {

    private final AddressAllocator addressAllocator;
    private final Ring<PeerAddress> ring;
    private final Set<Callback<Identifier>> callbacks = new HashSet<>();
    private final AddressType acceptedType;

    @Inject
    public RingBasedDht(AddressType acceptedType, AddressAllocator addressAllocator, Ring<PeerAddress> ring) {
        this.addressAllocator = addressAllocator;
        this.ring = ring;
        this.acceptedType = acceptedType;
    }

    @Override
    public void registerUncertainAddress(PeerAddress address, Identifier topic) {
        // TODO: find a better way of handling uncertain addresses
        registerAddress(address, topic);
    }

    @Override
    public void registerAddress(PeerAddress address, Identifier topic) {
        if(topic == null || topic.isEmpty()) {
            // Address not related to any particular topic
        } else {
            addressAllocator.addAddress(topic, address);
            demandPossiblyChanged(topic);
        }

        ring.insert(address);
    }

    @Override
    public void removeAddress(PeerAddress address) {
        ring.remove(address);
        addressAllocator.delAddressFromAllLines(address);
    }

    @Override
    public Set<PeerAddress> getInterested(Identifier topic, int maxResults) {
        return addressAllocator.getPeers(topic, maxResults);
    }

    @Override
    public Set<PeerAddress> getNearest(Identifier topic, int maxResults) {
        return ring.getNearest(topic, maxResults);
    }

    @Override
    public void addBounty(Identifier topic, Callback<PeerAddress> callback) {
        addressAllocator.addSlot(topic, callback);
        demandPossiblyChanged(topic);
    }

    @Override
    public void delBounty(Identifier topic, Callback<PeerAddress> callback) {
        addressAllocator.delSlot(topic, callback);
        demandPossiblyChanged(topic);
    }

    private void demandPossiblyChanged(Identifier topic) {
        for(Callback<Identifier> cb: callbacks) {
            cb.call(topic);
        }
    }

    public Provider<Integer> getDemand(Identifier topic) {
        return addressAllocator.getDemandProvider(topic);
    }

    public void onUpdate(Callback<Identifier> callback) {
        callbacks.add(callback);
    }

    @Override
    public AddressType getAcceptedType() {
        return acceptedType;
    }
}
