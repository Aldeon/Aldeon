package org.aldeon.networking.mediums.ip.nat.utils;

import org.aldeon.networking.common.Port;

import java.net.InetAddress;

public class StaticAddressTranslation implements AddressTranslation {

    private final InetAddress internalAddress;
    private final InetAddress externalAddress;
    private final Port internal;
    private final Port external;

    public StaticAddressTranslation(InetAddress internalAddress, Port internal, InetAddress externalAddress, Port external) {
        this.internal = internal;
        this.external = external;
        this.internalAddress = internalAddress;
        this.externalAddress = externalAddress;
    }

    @Override
    public Port getInternalPort() {
        return internal;
    }

    @Override
    public Port getExternalPort() {
        return external;
    }

    @Override
    public InetAddress getInternalAddress() {
        return internalAddress;
    }

    @Override
    public InetAddress getExternalAddress() {
        return externalAddress;
    }

    @Override
    public void shutdown() {
        // do nothing
    }
}
