package org.aldeon.utils.codec.base64;

import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.conversion.ConversionException;

import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;

class DatatypeConverterBasedBase64 implements Codec {

    @Override
    public String encode(ByteBuffer buffer) {
        byte[] b = new byte[buffer.remaining()];
        buffer.get(b);
        return DatatypeConverter.printBase64Binary(b);
    }

    @Override
    public ByteBuffer decode(String base64) throws ConversionException {
        try {
            return ByteBuffer.wrap(DatatypeConverter.parseBase64Binary(base64));
        } catch(Exception e) {
            throw new ConversionException("Failed to decode a base64 string into ByteBuffer", e);
        }
    }
}
