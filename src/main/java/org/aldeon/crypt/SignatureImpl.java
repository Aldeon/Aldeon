package org.aldeon.crypt;

import org.aldeon.model.FixedSizeImmutableByteBufferSource;

import java.nio.ByteBuffer;

public class SignatureImpl extends FixedSizeImmutableByteBufferSource implements Signature {
    public SignatureImpl(ByteBuffer buffer, boolean shouldCopy) throws IllegalArgumentException{
        super(buffer, shouldCopy, Signature.LENGTH_BYTES);
    }
}
