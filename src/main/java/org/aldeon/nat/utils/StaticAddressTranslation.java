package org.aldeon.nat.utils;

import org.aldeon.net.AddressTranslation;
import org.aldeon.net.Port;

import java.net.InetAddress;

public class StaticAddressTranslation implements AddressTranslation {

    private final InetAddress internalAddress;
    private final InetAddress externalAddress;
    private final Port internal;
    private final Port external;

    public StaticAddressTranslation(Port internal, Port external, InetAddress internalAddress, InetAddress getExternalAddress) {
        this.internal = internal;
        this.external = external;
        this.internalAddress = internalAddress;
        this.externalAddress = getExternalAddress;
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
