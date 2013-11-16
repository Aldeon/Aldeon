package org.aldeon.crypt;

import org.aldeon.crypt.exception.DecryptionFailedException;
import org.aldeon.crypt.exception.EncryptionFailedException;
import org.aldeon.model.ByteSource;
import org.aldeon.utils.math.ByteBufferArithmetic;

import java.nio.ByteBuffer;
import java.util.Comparator;

public class SignerImpl implements Signer {

    private Hash hash;
    private Comparator<ByteBuffer> equalityVerifier;

    public SignerImpl() {
        hash = new Sha256();
        equalityVerifier = new ByteBufferArithmetic();
    }

    @Override
    public void clear() {
        hash.clear();
    }

    @Override
    public void add(ByteBuffer buffer) {
        hash.add(buffer);
    }

    @Override
    public void add(ByteSource byteSource) {
        hash.add(byteSource);
    }

    @Override
    public void add(byte[] bytes) {
        hash.add(bytes);

    }

    @Override
    public Signature sign(Key key) {
        try {
            return new SignatureImpl(key.encrypt(hash.calculate()), false);
        } catch (EncryptionFailedException e) {
            throw new IllegalArgumentException("Failed to encrypt the data using given key", e);
        }
    }

    @Override
    public boolean verify(Key key, Signature signature) {
        try {
            return 0 == equalityVerifier.compare(hash.calculate(), key.decrypt(signature.getByteBuffer()));
        } catch (DecryptionFailedException e) {
            return false;
        }
    }
}
