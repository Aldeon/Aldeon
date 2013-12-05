package org.aldeon.utils.base64;

import org.aldeon.utils.conversion.ConversionException;

import java.nio.ByteBuffer;

public interface Base64 {
    String encode(ByteBuffer buffer);
    ByteBuffer decode(String base64) throws ConversionException;
}
