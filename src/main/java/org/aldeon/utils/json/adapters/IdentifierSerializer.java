package org.aldeon.utils.json.adapters;

import org.aldeon.model.Identifier;
import org.aldeon.utils.base64.Base64;

public class IdentifierSerializer extends ByteSourceSerializer<Identifier> {
    public IdentifierSerializer(Base64 base64) {
        super(base64);
    }
}
