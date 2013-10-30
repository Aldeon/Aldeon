package org.aldeon.crypt;

import org.aldeon.model.FixedSizeByteBufferSource;

import java.nio.ByteBuffer;

public class SignatureImpl extends FixedSizeByteBufferSource implements Signature {
    public SignatureImpl(ByteBuffer buffer, boolean shouldCopy) throws IllegalArgumentException{
        super(buffer, shouldCopy, Signature.LENGTH_BYTES);
    }
}
