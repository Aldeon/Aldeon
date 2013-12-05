package org.aldeon.utils.json.adapters;

import org.aldeon.crypt.Key;
import org.aldeon.utils.base64.Base64;

public class KeySerializer extends ByteSourceSerializer<Key> {
    public KeySerializer(Base64 base64) {
        super(base64);
    }
}
