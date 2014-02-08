package org.aldeon.model;

import org.aldeon.model.impl.FixedSizeImmutableByteBufferSource;

import java.nio.ByteBuffer;

public /* final */ class Signature extends FixedSizeImmutableByteBufferSource implements ByteSource {

    public static int LENGTH_BITS = 1024;
    public static int LENGTH_BYTES = LENGTH_BITS / 8;

    public Signature(ByteBuffer buffer, boolean shouldCopy) throws IllegalArgumentException{
        super(buffer, shouldCopy, Signature.LENGTH_BYTES);
    }
}
