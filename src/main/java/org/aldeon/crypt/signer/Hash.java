package org.aldeon.crypt.signer;

import org.aldeon.model.ByteSource;

import java.nio.ByteBuffer;

public interface Hash {
    void clear();

    void add(ByteBuffer buffer);
    void add(ByteSource byteSource);
    void add(byte[] bytes);

    ByteBuffer calculate();

    int length();
}
