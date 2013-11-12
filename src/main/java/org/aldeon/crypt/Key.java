package org.aldeon.crypt;

import org.aldeon.model.ByteSource;

import java.nio.ByteBuffer;

public interface Key extends ByteSource {

    ByteBuffer encrypt(ByteBuffer data);
    ByteBuffer decrypt(ByteBuffer data);

}
