package org.aldeon.networking.mediums.ip;

import com.google.common.collect.Sets;
import org.aldeon.networking.common.NetworkMedium;
import org.aldeon.networking.common.NewPeerAddressType;
import org.aldeon.networking.common.RecvPoint;
import org.aldeon.networking.common.SendPoint;
import org.aldeon.networking.mediums.ip.addresses.NewIpPeerAddress;

import java.nio.ByteBuffer;
import java.util.Set;

public class IpNetworkMedium implements NetworkMedium {

    @Override
    public Set<NewPeerAddressType> addressTypes() {
        return Sets.newHashSet(new NewPeerAddressType("IPV4"), new NewPeerAddressType("IPV6"));
    }

    @Override
    public NewIpPeerAddress createAddress(ByteBuffer source) {
        return NewIpPeerAddress.create(source);
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
    public NewIpPeerAddress getMachineAddress(NewPeerAddressType addressType) {
        return null;
    }
}
