package org.aldeon.utils.json.adapters;

import org.aldeon.crypt.Signature;
import org.aldeon.utils.base64.Base64Codec;

public class SignatureSerializer extends ByteSourceSerializer<Signature> {
    public SignatureSerializer(Base64Codec base64) {
        super(base64);
    }
}
