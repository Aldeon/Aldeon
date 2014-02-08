package org.aldeon.networking.mediums.ip.addresses;

import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.Port;
import org.aldeon.utils.net.PortImpl;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public abstract class IpPeerAddress implements PeerAddress {

    public static final AddressType IPV4 = new AddressType("IPV4");
    public static final AddressType IPV6 = new AddressType("IPV6");

    public abstract InetAddress getHost();
    public abstract Port getPort();

    public static IpPeerAddress create(ByteBuffer buffer) {
        throw new IllegalStateException("Not yet implemented");
    }

    @Deprecated
    public static IpPeerAddress create(String ip, int port) {
        try {
            InetAddress host = InetAddress.getByName(ip);
            return create(host, port);
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public static IpPeerAddress create(InetAddress host, int port) {

        Port portClass = new PortImpl(port);

        if(host instanceof Inet4Address) {
            return new IpV4PeerAddress((Inet4Address) host, portClass);
        }

        if(host instanceof Inet6Address) {
            return new IpV6PeerAddress((Inet6Address) host, portClass);
        }

        return null;
    }

    public static IpPeerAddress create(InetSocketAddress socketAddress) {
        return create(socketAddress.getAddress(), socketAddress.getPort());
    }

    @Override
    public String toString() {
        return getHost().getHostAddress() + ":" + getPort();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof IpPeerAddress) {
            IpPeerAddress addr = (IpPeerAddress) obj;
            return addr.getHost().equals(getHost()) && (addr.getPort().getIntValue() == getPort().getIntValue());
        } else return false;
    }

    @Override
    public int hashCode() {
        return getHost().hashCode() + getPort().hashCode();
    }
}
