package org.aldeon.net;

import org.aldeon.model.Identifier;
import org.aldeon.utils.net.PortImpl;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Ipv4PeerAddress implements IpPeerAddress {

    private final Inet4Address host;
    private final Port port;

    public Ipv4PeerAddress(Inet4Address host, Port port) {
        this.host = host;
        this.port = port;
    }

    @Deprecated
    public static Ipv4PeerAddress parse(String host, int port) {

        try {
            Port p = new PortImpl(port);
            InetAddress addr = InetAddress.getByName(host);
            if(addr instanceof Inet4Address) {
                return new Ipv4PeerAddress((Inet4Address) addr, p);
            }
        } catch (UnknownHostException e) { }
        throw new IllegalArgumentException("Invalid address input");
    }

    @Override
    public Port getPort() {
        return port;
    }

    @Override
    public Inet4Address getHost() {
        return host;
    }

    @Override
    public Identifier getIdentifier() {
        throw new IllegalStateException("Not yet implemented");
    }
}
