package org.aldeon.utils.base64;

import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;

/**
 * Example base64 codec.
 *
 * ACHTUNG: the '=' char is replaced with '-'.
 * This way the identifiers are not escaped when sent in json, so we avoid
 * using \u003d instead of '='.
 *
 */
public class Base64CodecImpl implements Base64Codec {
    @Override
    public String encode(ByteBuffer buffer) {
        byte[] b = new byte[buffer.remaining()];   // TODO remove unnecessary allocation
        buffer.get(b);
        return DatatypeConverter.printBase64Binary(b).replace('=', '-');
    }

    @Override
    public ByteBuffer decode(String base64) {
        return ByteBuffer.wrap(DatatypeConverter.parseBase64Binary(base64.replace('-', '=')));
    }
}
