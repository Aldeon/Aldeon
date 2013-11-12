package org.aldeon.crypt;

import org.aldeon.model.Id;
import org.aldeon.model.Identifier;

public class ExampleSigner {
    public static void main(String[] args) {

        // Key pair generator
        KeyGen gen = new RsaKeyGen();

        // Example key pair
        KeyGen.KeyPair pair = gen.generate();

        // Sign class
        Signer signer = new SignerImpl();


        // -----------------------------------------------------
        Identifier identifier = Id.random();

        // 1. Sign data using a private key
        signer.add("This is some signed content.".getBytes());
        signer.add(identifier);

        Signature s = signer.sign(pair.privateKey);


        // 2. Verify if the signature is correct (using a matching key)
        signer.add("This is some signed content.".getBytes());
        signer.add(identifier);

        System.out.println("Is valid? " + signer.verify(pair.publicKey, s));

    }
}
