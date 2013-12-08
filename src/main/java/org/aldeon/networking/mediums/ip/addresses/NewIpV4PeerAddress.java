package org.aldeon.networking.mediums.ip.addresses;

import org.aldeon.crypt.Hash;
import org.aldeon.crypt.signer.Sha256;
import org.aldeon.model.Identifier;
import org.aldeon.net.Port;
import org.aldeon.networking.common.NewPeerAddressType;

import java.net.Inet4Address;
import java.nio.ByteBuffer;

public class NewIpV4PeerAddress extends NewIpPeerAddress {

    private static final NewPeerAddressType type = new NewPeerAddressType("IPV4");
    private final Inet4Address host;
    private final Port port;
    private Identifier id;

    public NewIpV4PeerAddress(Inet4Address host, Port port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Inet4Address getInetAddress() {
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
