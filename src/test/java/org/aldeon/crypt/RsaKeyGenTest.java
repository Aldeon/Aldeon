package org.aldeon.crypt;

import org.aldeon.crypt.exception.DecryptionFailedException;
import org.aldeon.crypt.exception.EncryptionFailedException;
import org.aldeon.crypt.exception.KeyParseException;
import org.aldeon.utils.helpers.BufPrint;
import org.aldeon.utils.helpers.ByteBuffers;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RsaKeyGenTest {

    public final static byte[] inputBytes = "Hello World".getBytes();

    @Test
    public void shouldCreateMatchingKeysForEncryptionWithPrivateKeyAndDecryptionWithPublicKey() throws EncryptionFailedException, DecryptionFailedException {

        KeyGen gen = new RsaKeyGen();

        KeyGen.KeyPair pair = gen.generate();

        ByteBuffer input = ByteBuffer.wrap(inputBytes);
        ByteBuffer crypt = pair.privateKey.encrypt(input);
        ByteBuffer result = pair.publicKey.decrypt(crypt);

        assertTrue(ByteBuffers.equal(input, result));
    }

    @Test
    public void shouldCreateMatchingKeysForEncryptionWithPublicKeyAndDecryptionWithPrivateKey() throws EncryptionFailedException, DecryptionFailedException {

        KeyGen gen = new RsaKeyGen();

        KeyGen.KeyPair pair = gen.generate();

        ByteBuffer input = ByteBuffer.wrap(inputBytes);
        ByteBuffer crypt = pair.publicKey.encrypt(input);
        ByteBuffer result = pair.privateKey.decrypt(crypt);

        assertTrue(ByteBuffers.equal(input, result));
    }

    @Test(expected = DecryptionFailedException.class)
    public void shouldDecodeOnlyWithMatchingPublicKey() throws EncryptionFailedException, DecryptionFailedException {

        KeyGen gen = new RsaKeyGen();

        KeyGen.KeyPair pair1 = gen.generate();
        KeyGen.KeyPair pair2 = gen.generate();

        ByteBuffer input = ByteBuffer.wrap(inputBytes);
        ByteBuffer crypt = pair1.publicKey.encrypt(input);
        pair2.privateKey.decrypt(crypt);
    }

    @Test(expected = DecryptionFailedException.class)
    public void shouldDecodeOnlyWithMatchingPrivateKey() throws EncryptionFailedException, DecryptionFailedException {

        KeyGen gen = new RsaKeyGen();

        KeyGen.KeyPair pair1 = gen.generate();
        KeyGen.KeyPair pair2 = gen.generate();

        ByteBuffer input = ByteBuffer.wrap(inputBytes);
        ByteBuffer crypt = pair1.privateKey.encrypt(input);
        pair2.publicKey.decrypt(crypt);
    }

    @Test
    public void shouldCreateDifferentPairsEachTimeGenerateIsInvoked() {

        KeyGen gen = new RsaKeyGen();

        KeyGen.KeyPair pair1 = gen.generate();
        KeyGen.KeyPair pair2 = gen.generate();

        assertFalse(ByteBuffers.equal(pair1.publicKey.getByteBuffer(), pair2.publicKey.getByteBuffer()));
        assertFalse(ByteBuffers.equal(pair1.privateKey.getByteBuffer(), pair2.privateKey.getByteBuffer()));
    }

    @Test
    public void parsedKeyShouldBeIdenticalToOriginalKey() throws EncryptionFailedException, DecryptionFailedException, KeyParseException {

        KeyGen gen = new RsaKeyGen();

        KeyGen.KeyPair pair = gen.generate();

        ByteBuffer input = ByteBuffer.wrap(inputBytes);

        ByteBuffer bytePubKey = pair.publicKey.getByteBuffer();
        Key decodedPubKey = gen.parsePublicKey(bytePubKey);

        ByteBuffer crypt = decodedPubKey.encrypt(input);
        ByteBuffer result = pair.privateKey.decrypt(crypt);

        assertTrue(ByteBuffers.equal(input, result));
    }
}
