package org.aldeon.networking.mediums.ip.addresses;

import org.aldeon.model.Identifier;
import org.aldeon.net.Port;
import org.aldeon.networking.common.NewPeerAddressType;

import java.net.Inet6Address;

public class NewIpV6PeerAddress extends NewIpPeerAddress {

    private static NewPeerAddressType type = new NewPeerAddressType("IPV6");
    private final Inet6Address host;
    private final Port port;

    public NewIpV6PeerAddress(Inet6Address host, Port port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Inet6Address getInetAddress() {
        return host;
    }

    @Override
    public Port getPort() {
        return port;
    }

    @Override
    public NewPeerAddressType getType() {
        return type;
    }

    @Override
    public Identifier getIdentifier() {
        throw new IllegalStateException("Not yet implemented");
    }
}
