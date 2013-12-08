package org.aldeon.networking.mediums.ip;

import com.google.common.collect.Sets;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.NetworkMedium;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.RecvPoint;
import org.aldeon.networking.common.SendPoint;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;

import java.nio.ByteBuffer;
import java.util.Set;

public class IpNetworkMedium implements NetworkMedium {

    private static final int PORT = 41530;

    @Override
    public Set<AddressType> addressTypes() {
        return Sets.newHashSet(IpPeerAddress.IPV4, IpPeerAddress.IPV6);
    }

    @Override
    public SendPoint sendPoint() {
        return null;
    }

    @Override
    public RecvPoint recvPoint() {
        return null;
    }

    @Override
    public IpPeerAddress getMachineAddress(AddressType addressType) {
        return null;
    }

    @Override
    public String serialize(PeerAddress address) {
        return null;
    }

    @Override
    public PeerAddress deserialize(String address) {
        return null;
    }
}
