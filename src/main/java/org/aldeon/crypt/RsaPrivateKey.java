package org.aldeon.crypt;

import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Created with IntelliJ IDEA.
 * User: Prophet
 * Date: 08.11.13
 * Time: 15:26
 * To change this template use File | Settings | File Templates.
 */
public class RsaPrivateKey extends RsaKey{
    public RsaPrivateKey(Key k, SecureRandom s) {
        super(k, s);
    }

    public RsaPrivateKey(ByteBuffer b) throws NoSuchAlgorithmException, InvalidKeySpecException {
        super(KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(b.array())));
    }


}

