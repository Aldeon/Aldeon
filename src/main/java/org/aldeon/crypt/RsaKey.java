package org.aldeon.crypt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

class RsaKey implements Key {

    private static final String METHOD = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

    private final ByteBuffer raw;
    private final SecureRandom seed;
    private final java.security.Key key;

    public RsaKey(java.security.Key key, ByteBuffer buf, SecureRandom seed) {
        this.raw = buf;
        this.key = key;
        this.seed = seed;
    }

    private ByteBuffer process(ByteBuffer input, int mode)
            throws
            NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            BadPaddingException,
            ShortBufferException,
            IllegalBlockSizeException
    {
        Cipher cipher = Cipher.getInstance(METHOD);
        cipher.init(mode, key, seed);

        /*
            Due to padding, the output length is not known until the decryption is performed.
            Sadly, the cipher API expects an allocated output ByteBuffer to be provided before
            the process. Therefore we need to fall back to using ordinary byte arrays here.
         */

        byte[] in;
        if(input.hasArray()) {
            in = input.array();
        } else {
            in = new byte[input.remaining()];
            input.get(in);
        }

        return ByteBuffer.wrap(cipher.doFinal(in));
    }

    @Override
    public ByteBuffer encrypt(ByteBuffer data) {
        try {
            return process(data, Cipher.ENCRYPT_MODE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ByteBuffer decrypt(ByteBuffer data) {
        try {
            return process(data, Cipher.DECRYPT_MODE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ByteBuffer getByteBuffer() {
        ByteBuffer buf = raw.asReadOnlyBuffer();
        buf.rewind();
        return buf;
    }
}