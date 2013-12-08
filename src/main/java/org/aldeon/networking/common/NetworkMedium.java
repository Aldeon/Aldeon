package org.aldeon.networking.common;

import java.nio.ByteBuffer;
import java.util.Set;

public interface NetworkMedium {

    Set<NewPeerAddressType> addressTypes();
    NewPeerAddress createAddress(ByteBuffer source);

    SendPoint sendPoint();
    RecvPoint recvPoint();

    NewPeerAddress getMachineAddress(NewPeerAddressType addressType);
}
