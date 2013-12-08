package org.aldeon.networking.mediums.ip;

import com.google.common.collect.Sets;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.NetworkMedium;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.RecvPoint;
import org.aldeon.networking.common.SendPoint;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;
import org.aldeon.networking.mediums.ip.receiver.NettyRecvPoint;
import org.aldeon.networking.mediums.ip.sender.NettySendPoint;

import java.util.Set;

public class IpNetworkMedium implements NetworkMedium {

    private static final int PORT = 41530;
    private final RecvPoint recvPoint;
    private final SendPoint sendPoint;

    public IpNetworkMedium() {

        IpPeerAddress loopback = IpPeerAddress.create("0.0.0.0", 8080);

        recvPoint = new NettyRecvPoint(loopback);
        sendPoint = new NettySendPoint();

    }

    @Override
    public Set<AddressType> addressTypes() {
        return Sets.newHashSet(IpPeerAddress.IPV4, IpPeerAddress.IPV6);
    }

    @Override
    public SendPoint sendPoint() {
        return sendPoint;
    }

    @Override
    public RecvPoint recvPoint() {
        return recvPoint;
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
