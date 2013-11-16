package org.aldeon.crypt;

import org.aldeon.crypt.exception.DecryptionFailedException;
import org.aldeon.crypt.exception.EncryptionFailedException;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;

public class ExampleEncDec {
    public static void main(String[] args) throws NoSuchAlgorithmException, EncryptionFailedException, DecryptionFailedException {

        // Instantiate the generator
        KeyGen gen = new RsaKeyGen();

        // And this is our pair
        KeyGen.KeyPair pair = gen.generate();


        ByteBuffer input = ByteBuffer.wrap("Hello world".getBytes());

        ByteBuffer secret = pair.privateKey.encrypt(input);

        ByteBuffer output = pair.publicKey.decrypt(secret);

        String out = new String(output.array());

        System.out.println(out);

    }
}
