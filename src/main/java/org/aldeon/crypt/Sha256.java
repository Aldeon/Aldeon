package org.aldeon.crypt;

import org.aldeon.model.ByteSource;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256 implements Hash {

    private static final String ALGORITHM = "SHA-256";
    private static final int LENGTH_BITS = 256;
    private static final int LENGTH_BYTES = LENGTH_BITS / 8;

    private MessageDigest md;

    public Sha256() {
        clear();
    }

    @Override
    public void clear() {
        try {
            md = MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); // This should never happen
        }
    }

    @Override
    public void add(ByteBuffer buffer) {
        md.update(buffer);
    }

    @Override
    public void add(ByteSource byteSource) {
        md.update(byteSource.getByteBuffer());
    }

    @Override
    public void add(byte[] bytes) {
        md.update(bytes);
    }

    @Override
    public ByteBuffer calculate() {
        ByteBuffer result = ByteBuffer.wrap(md.digest());
        clear();
        return result;
    }

    @Override
    public int length() {
        return LENGTH_BYTES;
    }
}
