package org.aldeon.crypt;

import org.aldeon.model.ByteSource;

import java.nio.ByteBuffer;

public interface Signer {
    void clear();

    void add(ByteBuffer buffer);
    void add(ByteSource byteSource);
    void add(byte[] bytes);

    Signature sign(Key key);
    boolean verify(Key key, Signature signature);
}
