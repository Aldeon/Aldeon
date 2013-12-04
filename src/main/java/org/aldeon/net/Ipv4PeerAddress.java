package org.aldeon.net;

import org.aldeon.crypt.Hash;
import org.aldeon.crypt.Sha256;
import org.aldeon.model.Identifier;
import org.aldeon.utils.net.PortImpl;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class Ipv4PeerAddress implements IpPeerAddress {

    public static final String TYPE = "ipv4";

    private final Inet4Address host;
    private final Port port;
    private Identifier id = null;

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

        if(id == null) {
            ByteBuffer buf = ByteBuffer.allocate(6);
            buf.position(2);
            buf.putInt(port.getIntValue());
            buf.position(0);
            buf.put(host.getAddress());
            buf.position(0);

            Hash sha = new Sha256();
            sha.add(buf);
            id = Identifier.fromByteBuffer(sha.calculate(), false);
        }

        return id;
    }
}
