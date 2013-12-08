package org.aldeon.networking.common;

import java.nio.ByteBuffer;

public interface Transmission {
    NewPeerAddress address();
    ByteBuffer data();
}
