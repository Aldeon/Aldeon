package org.aldeon.utils.helpers;

import org.aldeon.model.Identifier;
import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.codec.base64.Base64Module;
import org.aldeon.utils.conversion.ConversionException;

import java.nio.ByteBuffer;
import java.util.Random;

public class Identifiers {

    private static final Random rand;

    private static final Codec base64;

    static {
        rand = new Random();
        base64 = new Base64Module().get();
    }

    @Deprecated
    public static Identifier random() {
        ByteBuffer buf = ByteBuffer.allocate(Identifier.LENGTH_BYTES);
        rand.nextBytes(buf.array());
        return Identifier.fromByteBuffer(buf, false);
    }

    @Deprecated
    public static Identifier fromBase64(String input) {
        try {
            return Identifier.fromByteBuffer(base64.decode(input), false);
        } catch (ConversionException e) {
            throw new IllegalArgumentException();
        }
    }
}
