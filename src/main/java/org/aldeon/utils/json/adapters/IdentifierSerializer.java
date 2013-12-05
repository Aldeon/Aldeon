package org.aldeon.utils.json.adapters;

import org.aldeon.model.Identifier;
import org.aldeon.utils.codec.Codec;

public class IdentifierSerializer extends ByteSourceSerializer<Identifier> {
    public IdentifierSerializer(Codec codec) {
        super(codec);
    }
}
