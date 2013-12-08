package org.aldeon.networking;

import com.google.inject.AbstractModule;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.PeerAddress;

public class NetworkingModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    public static String serialize(PeerAddress address) {
        return "";
    }

    public static PeerAddress deserialize(AddressType type, String address) {
        return null;
    }
}
