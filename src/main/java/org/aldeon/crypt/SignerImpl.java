package org.aldeon.crypt;

import org.aldeon.model.ByteSource;
import org.aldeon.utils.math.ByteBufferArithmetic;

import java.nio.ByteBuffer;
import java.util.Comparator;

public class SignerImpl implements Signer {

    private Hash hash;
    private Comparator<ByteBuffer> equalityVerifier;

    public SignerImpl() {
        clear();
        equalityVerifier = new ByteBufferArithmetic();
    }

    @Override
    public void clear() {
        hash = new Sha256();
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
        return new SignatureImpl(key.encrypt(hash.calculate()), false);
    }

    @Override
    public boolean verify(Key key, Signature signature) {
        return 0 == equalityVerifier.compare(hash.calculate(), key.decrypt(signature.getByteBuffer()));
    }
}
