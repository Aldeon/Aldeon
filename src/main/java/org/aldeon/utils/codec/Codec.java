package org.aldeon.utils.codec;

import org.aldeon.utils.conversion.ConversionException;

import java.nio.ByteBuffer;

public interface Codec {
    String encode(ByteBuffer buffer);
    ByteBuffer decode(String base64) throws ConversionException;
}
