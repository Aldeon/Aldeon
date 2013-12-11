package org.aldeon.crypt.rsa;


import org.aldeon.crypt.Key;
import org.aldeon.crypt.KeyGen;
import org.aldeon.crypt.exception.KeyParseException;
import org.aldeon.utils.helpers.BufPrint;
import org.aldeon.utils.helpers.ByteBuffers;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class RsaKeyGen implements KeyGen {

    public static final int SIZE_BITS = 1024;
    public static final int SIZE_BYTES = SIZE_BITS / 8;

    public static final BigInteger PUBLIC_EXPONENT = BigInteger.valueOf(65537);

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

        RSAPublicKey rsaPubKey = (RSAPublicKey) pub;

        if(! rsaPubKey.getPublicExponent().equals(PUBLIC_EXPONENT)) {
            throw new IllegalArgumentException();
        }

        BigInteger modulus = rsaPubKey.getModulus();
        byte[] modulusBytes = modulus.toByteArray();

        ByteBuffer modulusBuffer;

        if (modulusBytes[0] == 0x00) { // zero sign byte == positive integer
            modulusBuffer = ByteBuffer.allocate(modulusBytes.length - 1);
            modulusBuffer.put(modulusBytes, 1, modulusBuffer.capacity());
        } else {
            throw new IllegalStateException("Modulus must be a positive integer.");
        }

        KeyPair pair = new KeyPair();

        modulusBuffer.flip();

        pair.publicKey  = new RsaKey(pub, modulusBuffer, seed, Key.Type.PUBLIC);
        pair.privateKey = new RsaKey(prv, ByteBuffer.wrap(prv.getEncoded()), seed, Key.Type.PRIVATE);

        return pair;
    }

    @Override
    public Key parsePublicKey(ByteBuffer data) throws KeyParseException {

        data = data.asReadOnlyBuffer();

        if(data.remaining() == SIZE_BYTES)  {
            try {
                byte[] raw = new byte[data.capacity() + 1];

                raw[0] = 0x00;
                data.get(raw, 1, data.capacity());

                BigInteger modulus = new BigInteger(raw);

                RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, PUBLIC_EXPONENT);
                KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
                PublicKey pub = factory.generatePublic(spec);

                data.rewind();

                return new RsaKey(pub, data, seed, Key.Type.PUBLIC);
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                throw new KeyParseException("Invalid key structure", e);
            }
        } else {
            throw new KeyParseException("Invalid key size (expected: " + SIZE_BYTES + " bytes, was: " + data.remaining() + " bytes)");
        }
    }

    @Override
    public Key parsePrivateKey(ByteBuffer data) throws KeyParseException {
        try {
            ByteBuffer copy = ByteBuffers.clone(data);
            return new RsaKey(keyFactory.generatePrivate(new PKCS8EncodedKeySpec(copy.array())), copy, seed, Key.Type.PRIVATE);
        } catch (InvalidKeySpecException e) {
            throw new KeyParseException("Invalid key structure", e);
        }
    }
}
