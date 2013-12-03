package org.aldeon.model;

import org.aldeon.crypt.Key;
import org.aldeon.crypt.KeyGen;
import org.aldeon.crypt.RsaKeyGen;

public class Identities {

    public static Identity create(final String name) {

        KeyGen gen = new RsaKeyGen();

        final KeyGen.KeyPair pair = gen.generate();

        return create(name, pair.publicKey, pair.privateKey);
    }

    public static Identity create(final String name, final Key publicKey, final Key privateKey) {
        return new Identity() {
            @Override
            public Key getPrivateKey() {
                return privateKey;
            }

            @Override
            public Key getPublicKey() {
                return publicKey;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }
}
