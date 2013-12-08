package org.aldeon.networking;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provider;
import com.google.inject.multibindings.Multibinder;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.NetworkMedium;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.mediums.ip.IpNetworkMedium;

public class NetworkingModule extends AbstractModule implements Provider<NetworkState> {

    private static NetworkState networkState = null;

    @Override
    protected void configure() {
        Multibinder<NetworkMedium> networkMediumBinder = Multibinder.newSetBinder(binder(), NetworkMedium.class);

        /*
            Here we bind all available network mediums
         */

        // IP
        networkMediumBinder.addBinding().to(IpNetworkMedium.class);
    }

    public static String serialize(PeerAddress address) {
        return new NetworkingModule().get().serialize(address);
    }

    public static PeerAddress deserialize(AddressType type, String string) {
        return new NetworkingModule().get().deserialize(type, string);
    }

    @Override
    public NetworkState get() {
        if(networkState == null) {
            networkState = Guice.createInjector(this).getInstance(NetworkState.class);
        }
        return networkState;
    }
}
