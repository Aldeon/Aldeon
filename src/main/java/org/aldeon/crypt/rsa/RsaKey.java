package org.aldeon.crypt.rsa;

import org.aldeon.crypt.Key;
import org.aldeon.crypt.exception.DecryptionFailedException;
import org.aldeon.crypt.exception.EncryptionFailedException;
import org.aldeon.model.impl.FixedSizeImmutableByteBufferSource;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

class RsaKey extends FixedSizeImmutableByteBufferSource implements Key {

    private static final String METHOD = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

    private final SecureRandom seed;
    private final java.security.Key key;
    private final Type type;

    public RsaKey(java.security.Key key, ByteBuffer buf, SecureRandom seed, Type type) {
        super(buf, false, RsaKeyGen.SIZE_BYTES);
        this.key = key;
        this.seed = seed;
        this.type = type;
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

        ByteBuffer result =  ByteBuffer.wrap(cipher.doFinal(in));

        result.clear();
        return result;
    }

    @Override
    public ByteBuffer encrypt(ByteBuffer data) throws EncryptionFailedException {
        try {
            return process(data, Cipher.ENCRYPT_MODE);
        } catch (Exception e) {
            throw new EncryptionFailedException("Could not encrypt the given data", e);
        }
    }

    @Override
    public ByteBuffer decrypt(ByteBuffer data) throws DecryptionFailedException {
        try {
            return process(data, Cipher.DECRYPT_MODE);
        } catch (Exception e) {
            throw new DecryptionFailedException("Could not decrypt the given data", e);
        }
    }

    @Override
    public Type getType() {
        return type;
    }

}