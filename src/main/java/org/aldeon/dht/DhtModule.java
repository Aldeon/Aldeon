package org.aldeon.dht;

import com.google.inject.Binder;
import com.google.inject.Module;
import org.aldeon.net.PeerAddress;

public class DhtModule implements Module{
    @Override
    public void configure(Binder binder) {

    }

    public <T extends PeerAddress> Dht<T> createDht(Class<T> addressType) {

        return null;
    }
}
