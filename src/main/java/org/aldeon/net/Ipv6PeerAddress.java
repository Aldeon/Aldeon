package org.aldeon.net;

import org.aldeon.model.Identifier;

import java.net.Inet6Address;

public class Ipv6PeerAddress implements IpPeerAddress {

    public static final String TYPE = "ipv6";

    private final Inet6Address host;
    private final Port port;

    public Ipv6PeerAddress(Inet6Address host, Port port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Port getPort() {
        return port;
    }

    @Override
    public Inet6Address getHost() {
        return host;
    }

    @Override
    public Identifier getIdentifier() {
        throw new IllegalStateException("Not yet implemented");
    }
}
