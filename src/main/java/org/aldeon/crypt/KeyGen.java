package org.aldeon.crypt;

import java.nio.ByteBuffer;

public interface KeyGen {

    KeyPair generate();
    Key parsePublicKey(ByteBuffer data) throws IllegalArgumentException;
    Key parsePrivateKey(ByteBuffer data) throws IllegalArgumentException;

    public class KeyPair {
        public Key publicKey;
        public Key privateKey;
    }
}
