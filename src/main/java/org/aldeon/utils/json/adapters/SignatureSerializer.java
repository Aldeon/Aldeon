package org.aldeon.utils.json.adapters;

import org.aldeon.model.Signature;
import org.aldeon.utils.base64.Base64;

public class SignatureSerializer extends ByteSourceSerializer<Signature> {
    public SignatureSerializer(Base64 base64) {
        super(base64);
    }
}
