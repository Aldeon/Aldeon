package org.aldeon.networking.mediums.ip.addresses;

import org.aldeon.net.Port;
import org.aldeon.networking.common.NewPeerAddress;
import org.aldeon.utils.net.PortImpl;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public abstract class NewIpPeerAddress implements NewPeerAddress {

    public abstract InetAddress getInetAddress();
    public abstract Port getPort();

    public static NewIpPeerAddress create(ByteBuffer buffer) {
        return null;
    }

    @Deprecated
    public static NewIpPeerAddress create(String ip, int port) {
        try {
            InetAddress host = InetAddress.getByName(ip);
            return create(host, port);
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public static NewIpPeerAddress create(InetAddress host, int port) {

        Port portClass = new PortImpl(port);

        if(host instanceof Inet4Address) {
            return new NewIpV4PeerAddress((Inet4Address) host, portClass);
        }

        if(host instanceof Inet6Address) {
            return new NewIpV6PeerAddress((Inet6Address) host, portClass);
        }

        return null;
    }
}
