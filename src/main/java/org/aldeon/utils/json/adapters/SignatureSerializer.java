package org.aldeon.utils.json.adapters;

import org.aldeon.model.Signature;
import org.aldeon.utils.codec.Codec;

public class SignatureSerializer extends ByteSourceSerializer<Signature> {
    public SignatureSerializer(Codec codec) {
        super(codec);
    }
}
