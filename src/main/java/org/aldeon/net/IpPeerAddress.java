package org.aldeon.net;

import org.aldeon.utils.net.PortImpl;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

public abstract class IpPeerAddress implements PeerAddress {
    public abstract Port getPort();
    public abstract InetAddress getHost();

    @Override
    public int hashCode() {
        return getIdentifier().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof IpPeerAddress) {
            IpPeerAddress that = (IpPeerAddress) obj;
            return this.getPort().equals(that.getPort()) && this.getHost().equals(that.getHost());
        } else {
            return false;
        }
    }

    public static IpPeerAddress create(InetAddress host, Port port) {
        if(host instanceof Inet4Address) {
            return new Ipv4PeerAddress((Inet4Address) host, port);
        }

        if(host instanceof Inet6Address) {
            return new Ipv6PeerAddress((Inet6Address) host, port);
        }

        return null;
    }

    public static IpPeerAddress create(InetAddress host, int port) {
        return create(host, new PortImpl(port));
    }
}
