package org.aldeon.networking.mediums.ip.addresses;

import org.aldeon.crypt.Hash;
import org.aldeon.crypt.signer.Sha256;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.Port;

import java.net.Inet4Address;
import java.nio.ByteBuffer;

public class IpV4PeerAddress extends IpPeerAddress {

    private final Inet4Address host;
    private final Port port;
    private Identifier id;

    public IpV4PeerAddress(Inet4Address host, Port port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Inet4Address getHost() {
        return host;
    }

    @Override
    public Port getPort() {
        return port;
    }

    @Override
    public AddressType getType() {
        return IPV4;
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
