package org.aldeon.model;

import org.aldeon.model.impl.IdentifierImpl;
import org.aldeon.utils.base64.Base64Codec;
import org.aldeon.utils.base64.MiGBase64Impl;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.math.Arithmetic;
import org.aldeon.utils.math.ByteBufferArithmetic;

import java.nio.ByteBuffer;
import java.util.Random;

public class Id {

    private final static Identifier emptyId;
    private final static Random rand;
    private final static Arithmetic<ByteBuffer> arithmetic;
    private final static Base64Codec codec;

    static {
        emptyId = new IdentifierImpl(ByteBuffer.allocate(Identifier.LENGTH_BYTES), false);
        rand = new Random();
        arithmetic = new ByteBufferArithmetic();
        codec = new MiGBase64Impl();
    }

    public static Identifier fromByteBuffer(ByteBuffer buf, boolean copy) {
        return new IdentifierImpl(buf, copy);
    }

    public static Identifier fromBytes(byte[] bytes, boolean copy) {
        return new IdentifierImpl(ByteBuffer.wrap(bytes), copy);
    }

    public static Identifier fromBase64(String base64) throws ConversionException {
        return new IdentifierImpl(codec.decode(base64), false);
    }

    public static Identifier empty() {
        return emptyId;
    }

    public static boolean isEmpty(Identifier id) {
        return equal(empty(), id);
    }

    public static boolean equal(Identifier a, Identifier b) {
        return arithmetic.compare(a.getByteBuffer(), b.getByteBuffer()) == 0;
    }

    @Deprecated
    public static Identifier random() {
        ByteBuffer buf = ByteBuffer.allocate(Identifier.LENGTH_BYTES);
        rand.nextBytes(buf.array());
        return new IdentifierImpl(buf, false);
    }

    public static Identifier xor(Identifier a, Identifier b) {
        return new IdentifierImpl(arithmetic.xor(a.getByteBuffer(), b.getByteBuffer()), false);
    }

    public static String toString(Identifier a) {
        return codec.encode(a.getByteBuffer());
    }
}
