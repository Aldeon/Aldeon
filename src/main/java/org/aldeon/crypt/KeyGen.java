package org.aldeon.crypt;

import org.aldeon.crypt.exception.KeyParseException;

import java.nio.ByteBuffer;

public interface KeyGen {

    KeyPair generate();
    Key parsePublicKey(ByteBuffer data) throws KeyParseException;
    Key parsePrivateKey(ByteBuffer data) throws KeyParseException;

    public class KeyPair {
        public Key publicKey;
        public Key privateKey;

        public KeyPair() {

        }

        public KeyPair(Key publicKey, Key privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }
    }
}
