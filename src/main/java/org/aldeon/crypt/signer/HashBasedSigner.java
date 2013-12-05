package org.aldeon.crypt.signer;

import com.google.inject.Inject;
import org.aldeon.crypt.Hash;
import org.aldeon.crypt.Key;
import org.aldeon.crypt.Signer;
import org.aldeon.crypt.exception.DecryptionFailedException;
import org.aldeon.crypt.exception.EncryptionFailedException;
import org.aldeon.model.ByteSource;
import org.aldeon.model.Signature;
import org.aldeon.utils.helpers.ByteBuffers;

import java.nio.ByteBuffer;

class HashBasedSigner implements Signer {

    private final Hash hash;

    @Inject
    public HashBasedSigner(Hash hash) {
        this.hash = hash;
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
            return new Signature(key.encrypt(hash.calculate()), false);
        } catch (EncryptionFailedException e) {
            throw new IllegalArgumentException("Failed to encrypt the data using given key", e);
        }
    }

    @Override
    public boolean verify(Key key, Signature signature) {
        try {
            return 0 == ByteBuffers.compare(hash.calculate(), key.decrypt(signature.getByteBuffer()));
        } catch (DecryptionFailedException e) {
            return false;
        }
    }
}
