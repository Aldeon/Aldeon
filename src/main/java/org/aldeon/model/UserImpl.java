package org.aldeon.model;

import org.aldeon.crypt.Key;

public class UserImpl implements User {
    private Key publicKey;
    private String name;

    private UserImpl() { }

    public static UserImpl create(final String name, final Key publicKey) {

        if(publicKey.getType() == Key.Type.PUBLIC) {
            UserImpl userImpl = new UserImpl();

            userImpl.publicKey = publicKey;
            userImpl.name = name;

            return userImpl;
        } else {
            throw new IllegalArgumentException("Invalid key types");
        }
    }

    @Override
    public Key getPublicKey() {
        return publicKey;
    }

    @Override
    public String getName() {
        return name;
    }
}
