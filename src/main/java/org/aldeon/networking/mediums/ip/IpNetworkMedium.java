package org.aldeon.networking.mediums.ip;

import com.google.common.collect.Sets;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.NetworkMedium;
import org.aldeon.networking.common.RecvPoint;
import org.aldeon.networking.common.SendPoint;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;

import java.nio.ByteBuffer;
import java.util.Set;

public class IpNetworkMedium implements NetworkMedium {

    @Override
    public Set<AddressType> addressTypes() {
        return Sets.newHashSet(new AddressType("IPV4"), new AddressType("IPV6"));
    }

    @Override
    public IpPeerAddress createAddress(ByteBuffer source) {
        return IpPeerAddress.create(source);
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
}
