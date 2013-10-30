package org.aldeon.utils.base64;

import java.nio.ByteBuffer;

public interface Base64Codec {
    String encode(ByteBuffer buffer);
    ByteBuffer decode(String base64);
}
