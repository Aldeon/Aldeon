package org.aldeon.utils.base64;

import org.aldeon.utils.conversion.ConversionException;

import java.nio.ByteBuffer;

class Base64CharReplacer implements Base64 {

    private final Base64 base64;

    public Base64CharReplacer(Base64 base64) {
        this.base64 = base64;
    }

    @Override
    public String encode(ByteBuffer buffer) {
        return base64.encode(buffer).replace('=', '-').replace('+', '.');
    }

    @Override
    public ByteBuffer decode(String string) throws ConversionException {
        return base64.decode(string.replace('-', '=').replace('.', '+'));
    }
}
