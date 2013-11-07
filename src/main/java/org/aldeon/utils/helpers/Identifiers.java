package org.aldeon.utils.helpers;

import org.aldeon.model.Identifier;
import org.aldeon.model.IdentifierImpl;
import org.aldeon.utils.base64.Base64Codec;
import org.aldeon.utils.base64.Base64CodecImpl;
import org.aldeon.utils.math.Arithmetic;
import org.aldeon.utils.math.ByteBufferArithmetic;

import java.nio.ByteBuffer;

public class Identifiers {

    private static final Identifier emptyIdentifier;
    private static final Arithmetic<ByteBuffer> arithmetic;
    private static final Base64Codec base64;

    static {
        emptyIdentifier = new IdentifierImpl(ByteBuffer.allocate(Identifier.LENGTH_BYTES), false);
        arithmetic = new ByteBufferArithmetic();
        base64 = new Base64CodecImpl();
    }

    public static boolean isEmpty(Identifier id) {
        return equal(id, emptyIdentifier);
    }

    public static Identifier xor(Identifier a, Identifier b) {
        return new IdentifierImpl(arithmetic.xor(a.getByteBuffer(), b.getByteBuffer()), false);
    }

    public static String toString(Identifier id) {
        return base64.encode(id.getByteBuffer());
    }

    public static boolean equal(Identifier a, Identifier b) {
        return 0 == arithmetic.compare(a.getByteBuffer(), b.getByteBuffer());
    }

}
