package org.aldeon.networking.common;

import java.nio.ByteBuffer;
import java.util.Set;

public interface NetworkMedium {

    Set<AddressType> addressTypes();
    PeerAddress createAddress(ByteBuffer source);

    SendPoint sendPoint();
    RecvPoint recvPoint();

    PeerAddress getMachineAddress(AddressType addressType);
}
