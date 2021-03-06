package org.aldeon.model;

import org.aldeon.model.impl.FixedSizeImmutableByteBufferSource;
import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.codec.base64.Base64Module;
import org.aldeon.utils.helpers.ByteBuffers;

import java.nio.ByteBuffer;

/**
 * Uniquely identifies messages and peers.
 */
public /* final */ class Identifier extends FixedSizeImmutableByteBufferSource {
    public static final int LENGTH_BITS = 256;
    public static final int LENGTH_BYTES = LENGTH_BITS / 8;

    private final static Identifier emptyId;


    //////////////////////////////////////////////////////////////////////////////

    static {
        //emptyId = new Identifier(ByteBuffer.allocate(Identifier.LENGTH_BYTES), false);
        ByteBuffer bufferWithOnes = ByteBuffer.allocate(Identifier.LENGTH_BYTES);
        for(int i=0;i<LENGTH_BYTES;++i) {
            bufferWithOnes.put(i, (byte) 0xFF);
        }
        emptyId = new Identifier(bufferWithOnes, false);
    }

    public static Identifier fromByteBuffer(ByteBuffer buf, boolean copy) {
        return new Identifier(buf, copy);
    }

    public static Identifier fromBytes(byte[] bytes, boolean copy) {
        return new Identifier(ByteBuffer.wrap(bytes), copy);
    }

    public static Identifier empty() {
        return emptyId;
    }

    ////////////////////////////////////////////////////////////////////////////////

    private Identifier(ByteBuffer buffer, boolean shouldCopy) throws IllegalArgumentException {
        super(buffer, shouldCopy, Identifier.LENGTH_BYTES);
    }

    public boolean isEmpty() {
        return equals(empty());
    }

    public Identifier xor(Identifier id) {
        return Identifier.fromByteBuffer(ByteBuffers.xor(this.getByteBuffer(), id.getByteBuffer()), false);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Identifier) {
            Identifier that = (Identifier) obj;
            return 0 == ByteBuffers.compare(this.getByteBuffer(), that.getByteBuffer());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getByteBuffer().hashCode();
    }
}
