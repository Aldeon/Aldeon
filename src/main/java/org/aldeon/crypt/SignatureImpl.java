package org.aldeon.crypt;

import org.aldeon.model.FixedSizeImmutableByteBufferSource;
import org.aldeon.utils.base64.MiGBase64Impl;

import java.nio.ByteBuffer;

public class SignatureImpl extends FixedSizeImmutableByteBufferSource implements Signature {
    public SignatureImpl(ByteBuffer buffer, boolean shouldCopy) throws IllegalArgumentException{
        super(buffer, shouldCopy, Signature.LENGTH_BYTES);
    }

    public SignatureImpl(String base64string, boolean shouldCopy) throws IllegalArgumentException {
        //TODO: should use class implementing Base64Codec, replacing '-' <-> '=' is in *Impl class
        super(ByteBuffer.wrap(MiGBase64Impl.decodeToByteArray(base64string.replace('-', '='))),
              shouldCopy,
              Signature.LENGTH_BYTES);
    }
}
