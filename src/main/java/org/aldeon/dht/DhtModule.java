package org.aldeon.dht;

import com.google.inject.Binder;
import com.google.inject.Module;
import org.aldeon.communication.Sender;
import org.aldeon.dht.miners.DemandWatcher;
import org.aldeon.dht.miners.MinerManager;
import org.aldeon.dht.miners.MinerProvider;
import org.aldeon.dht.miners.MinerProviderImpl;

public class DhtModule implements Module{
    @Override
    public void configure(Binder binder) {

    }

    public static Dht createDht(Sender sender) {

        DhtImpl dht = new DhtImpl(sender.getAcceptedType());
        DemandWatcher watcher = dht;
        Dht wrapped = new DhtTypeCheckDecorator(dht);

        MinerProvider minerProvider = new MinerProviderImpl(sender, wrapped);
        new MinerManager(watcher, minerProvider);

        return wrapped;
    }
}
