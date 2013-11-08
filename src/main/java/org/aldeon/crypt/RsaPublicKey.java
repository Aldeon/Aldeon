package org.aldeon.crypt;

import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RsaPublicKey extends RsaKey{

    public RsaPublicKey(Key k, SecureRandom s) {
        super(k, s);
    }

    public RsaPublicKey(ByteBuffer b) throws NoSuchAlgorithmException, InvalidKeySpecException {
        super(KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(b.array())));
    }

}
