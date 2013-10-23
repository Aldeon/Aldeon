package org.aldeon.nat.utils;

import org.aldeon.common.net.AddressTranslation;
import org.aldeon.common.net.Port;

import java.net.InetAddress;

public class NoAddressTranslation implements AddressTranslation {

    private Port port;
    private InetAddress address;

    public NoAddressTranslation(Port port, InetAddress address) {
        this.port = port;
        this.address = address;
    }

    @Override
    public Port getInternalPort() {
        return port;
    }

    @Override
    public Port getExternalPort() {
        return port;
    }

    @Override
    public InetAddress getInternalAddress() {
        return address;
    }

    @Override
    public InetAddress getExternalAddress() {
        return address;
    }

    @Override
    public void shutdown() {
        // do nothing
    }
}
