package org.aldeon.model;

import java.nio.ByteBuffer;

public class IdentifierImpl extends FixedSizeImmutableByteBufferSource implements Identifier {
    public IdentifierImpl(ByteBuffer buffer, boolean shouldCopy) throws IllegalArgumentException{
        super(buffer, shouldCopy, Identifier.LENGTH_BYTES);
    }
}