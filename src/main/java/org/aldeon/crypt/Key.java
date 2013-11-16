package org.aldeon.crypt;

import org.aldeon.crypt.exception.DecryptionFailedException;
import org.aldeon.crypt.exception.EncryptionFailedException;
import org.aldeon.model.ByteSource;

import java.nio.ByteBuffer;

public interface Key extends ByteSource {

    ByteBuffer encrypt(ByteBuffer data) throws EncryptionFailedException;
    ByteBuffer decrypt(ByteBuffer data) throws DecryptionFailedException;

    Type getType();

    public static enum Type {
        PUBLIC,
        PRIVATE
    }
}
