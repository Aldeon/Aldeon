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
    }
}
