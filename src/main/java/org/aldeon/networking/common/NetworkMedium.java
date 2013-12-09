package org.aldeon.networking.common;

import java.util.Set;

public interface NetworkMedium {

    Set<AddressType> addressTypes();

    SendPoint sendPoint();
    RecvPoint recvPoint();

    PeerAddress getMachineAddress(AddressType addressType);

    String serialize(PeerAddress address);
    PeerAddress deserialize(String address);
}
