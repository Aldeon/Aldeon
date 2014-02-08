package org.aldeon.networking.common;

import java.nio.ByteBuffer;

public interface Transmission {
    PeerAddress address();
    ByteBuffer data();
}
