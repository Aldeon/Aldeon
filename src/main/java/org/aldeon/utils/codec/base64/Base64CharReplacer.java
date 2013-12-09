package org.aldeon.utils.codec.base64;

import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.conversion.ConversionException;

import java.nio.ByteBuffer;

class Base64CharReplacer implements Codec {

    private final Codec codec;

    public Base64CharReplacer(Codec codec) {
        this.codec = codec;
    }

    @Override
    public String encode(ByteBuffer buffer) {
        return codec.encode(buffer).replace('=', '-').replace('+', '.');
    }

    @Override
    public ByteBuffer decode(String string) throws ConversionException {
        return codec.decode(string.replace('-', '=').replace('.', '+'));
    }
}
