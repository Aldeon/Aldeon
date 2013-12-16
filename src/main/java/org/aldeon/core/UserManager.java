package org.aldeon.core;

import org.aldeon.crypt.Key;
import org.aldeon.model.Identity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private static final Map<String,Identity> identities = new HashMap<>();

    public Map<String,Identity> getAllIdentities() {
        return Collections.unmodifiableMap(identities);
    }

    public void addIdentity(Identity identity) {
        identities.put(identity.getPublicKey().toString(),identity);
    }

    public void delIdentity(Identity identity) {
        identities.remove(identity.getPublicKey());
    }

    public Identity getIdentity(Key publicKey){
        return identities.get(publicKey.toString());
    }
}
