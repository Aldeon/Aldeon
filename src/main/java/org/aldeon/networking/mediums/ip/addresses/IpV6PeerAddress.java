package org.aldeon.networking.mediums.ip.addresses;

import org.aldeon.model.Identifier;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.Port;

import java.net.Inet6Address;

public class IpV6PeerAddress extends IpPeerAddress {

    private static AddressType type = new AddressType("IPV6");
    private final Inet6Address host;
    private final Port port;

    public IpV6PeerAddress(Inet6Address host, Port port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Inet6Address getHost() {
        return host;
    }

    @Override
    public Port getPort() {
        return port;
    }

    @Override
    public AddressType getType() {
        return type;
    }

    @Override
    public Identifier getIdentifier() {
        throw new IllegalStateException("Not yet implemented");
    }
}
