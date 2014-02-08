package org.aldeon.utils.json.adapters;

import org.aldeon.crypt.Key;
import org.aldeon.utils.codec.Codec;

public class KeySerializer extends ByteSourceSerializer<Key> {
    public KeySerializer(Codec codec) {
        super(codec);
    }
}
