package org.aldeon.crypt;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RsaKeyGen implements KeyGen {

    public static final int SIZE_BITS = 1024;
    public static final int SIZE_BYTES = SIZE_BITS / 8;

    static{
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String ALGORITHM = "RSA";
    private static final String PROVIDER = "BC";

    private SecureRandom seed;
    private KeyFactory keyFactory;
    private KeyPairGenerator generator;

    public RsaKeyGen() {
        this(new SecureRandom());
    }

    public RsaKeyGen(SecureRandom seed) {

        this.seed = seed;

        try {
            generator = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER);
            generator.initialize(SIZE_BITS, seed);
            keyFactory = KeyFactory.getInstance(ALGORITHM);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();    // This should never happen
        }
    }

    @Override
    public KeyPair generate() {

        java.security.KeyPair kp = generator.generateKeyPair();

        java.security.Key pub = kp.getPublic();
        java.security.Key prv = kp.getPrivate();

        KeyPair pair = new KeyPair();
        pair.publicKey  = new RsaKey(pub, ByteBuffer.wrap(pub.getEncoded()), seed, Key.Type.PUBLIC);
        pair.privateKey = new RsaKey(prv, ByteBuffer.wrap(prv.getEncoded()), seed, Key.Type.PRIVATE);

        return pair;
    }

    @Override
    public Key parsePublicKey(ByteBuffer data) {
        try {
            ByteBuffer copy = cloneBuf(data);
            return new RsaKey(keyFactory.generatePublic(new X509EncodedKeySpec(copy.array())), copy, seed, Key.Type.PUBLIC);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Invalid key structure", e);
        }
    }

    @Override
    public Key parsePrivateKey(ByteBuffer data) {
        try {
            ByteBuffer copy = cloneBuf(data);
            return new RsaKey(keyFactory.generatePrivate(new PKCS8EncodedKeySpec(copy.array())), copy, seed, Key.Type.PRIVATE);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Invalid key structure", e);
        }
    }

    public static ByteBuffer cloneBuf(ByteBuffer original) {
        ByteBuffer clone = ByteBuffer.allocate(original.capacity());
        original.rewind(); // copy from the beginning
        clone.put(original);
        original.rewind();
        clone.flip();
        return clone;
    }
}
