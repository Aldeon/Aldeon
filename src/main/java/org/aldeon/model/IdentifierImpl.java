package org.aldeon.model;

import org.aldeon.utils.base64.MiGBase64Impl;

import java.nio.ByteBuffer;

public class IdentifierImpl extends FixedSizeImmutableByteBufferSource implements Identifier {
    public IdentifierImpl(ByteBuffer buffer, boolean shouldCopy) throws IllegalArgumentException{
        super(buffer, shouldCopy, Identifier.LENGTH_BYTES);
    }

    public IdentifierImpl(String base64string, boolean shouldCopy) throws IllegalArgumentException {
        //TODO: should use class implementing Base64Codec,replacing '-' <-> '=' is in *Impl class
        super(ByteBuffer.wrap(MiGBase64Impl.decodeToByteArray(base64string.replace('-', '='))),
              shouldCopy,
              Identifier.LENGTH_BYTES);
    }

    public static IdentifierImpl zeroIdentifier() {
        return new IdentifierImpl(ByteBuffer.allocate(Identifier.LENGTH_BYTES), false);
    }
}
