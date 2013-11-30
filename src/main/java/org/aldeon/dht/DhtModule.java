package org.aldeon.dht;

import com.google.inject.Binder;
import com.google.inject.Module;
import org.aldeon.communication.Sender;
import org.aldeon.dht.miners.MinerManager;
import org.aldeon.dht.miners.MinerProvider;
import org.aldeon.dht.miners.MinerProviderImpl;
import org.aldeon.net.PeerAddress;

import java.util.concurrent.Executor;

public class DhtModule implements Module{
    @Override
    public void configure(Binder binder) {

    }

    public static <T extends PeerAddress> Dht<T> createDht(Sender<T> sender, Executor executor, Class<T> addressType) {

        DhtImpl<T> dht = new DhtImpl<>();

        MinerProvider minerProvider = new MinerProviderImpl<>(sender, executor, addressType, dht);
        new MinerManager(dht, minerProvider);

        return dht;
    }
}
