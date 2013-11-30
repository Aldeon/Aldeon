package org.aldeon.dht;

import org.aldeon.dht.miners.DemandWatcher;
import org.aldeon.dht.ring.Ring;
import org.aldeon.dht.ring.RingImpl;
import org.aldeon.dht.slots.AddressAllocator;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;
import org.aldeon.utils.collections.Provider;
import org.aldeon.utils.math.ByteBufferArithmetic;

import java.util.HashSet;
import java.util.Set;

public class DhtImpl<T extends PeerAddress> implements Dht<T>, DemandWatcher {

    private final AddressAllocator<T> addressAllocator;
    private final Ring<T> ring;
    private final Set<Callback<Identifier>> callbacks;

    public DhtImpl() {
        addressAllocator = new AddressAllocator<>();
        ring = new RingImpl<>(new ByteBufferArithmetic());
        callbacks = new HashSet<>();
    }

    @Override
    public void registerUncertainAddress(T address, Identifier topic) {
        // TODO: find a better way of handling uncertain addresses
        registerAddress(address, topic);
    }

    @Override
    public void registerAddress(T address, Identifier topic) {
        if(topic == null || topic.isEmpty()) {
            // Address not related to any particular topic
        } else {
            addressAllocator.addAddress(topic, address);
            demandPossiblyChanged(topic);
        }

        ring.insert(address);
    }

    @Override
    public void removeAddress(T address) {
        ring.remove(address);
    }

    @Override
    public Set<T> getInterested(Identifier topic, int maxResults) {
        return addressAllocator.getPeers(topic, maxResults);
    }

    @Override
    public Set<T> getNearest(Identifier topic, int maxResults) {
        return ring.getNearest(topic, maxResults);
    }

    @Override
    public void addBounty(Identifier topic, Callback<T> callback) {
        addressAllocator.addSlot(topic, callback);
        demandPossiblyChanged(topic);
    }

    @Override
    public void delBounty(Identifier topic, Callback<T> callback) {
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
}
