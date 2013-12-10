package org.aldeon.dht;

import com.google.inject.Binder;
import com.google.inject.Module;
import org.aldeon.networking.common.Sender;
import org.aldeon.dht.miners.DemandWatcher;
import org.aldeon.dht.miners.MinerManager;
import org.aldeon.dht.miners.MinerProvider;
import org.aldeon.dht.miners.MinerProviderImpl;
import org.aldeon.dht.ring.RingImpl;
import org.aldeon.dht.slots.AddressAllocator;
import org.aldeon.networking.common.AddressType;

public class DhtModule implements Module {
    @Override
    public void configure(Binder binder) {

    }

    public static Dht createDht(Sender sender, AddressType acceptedType) {

        // TODO: make this more elegant

        RingBasedDht dht = new RingBasedDht(acceptedType, new AddressAllocator(), new RingImpl());
        DemandWatcher watcher = dht;
        Dht wrapped = new DhtTypeCheckDecorator(dht);

        MinerProvider minerProvider = new MinerProviderImpl(sender, wrapped);
        new MinerManager(watcher, minerProvider);

        return wrapped;
    }
}