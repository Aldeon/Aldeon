package org.aldeon.core.events;

import org.aldeon.crypt.Key;
import org.aldeon.events.Event;
import org.aldeon.model.Identity;

/**
 * Event triggered when user deletes one of identites
 */
public class IdentityRemovedEvent implements Event {
    private final Key userKey;

    public IdentityRemovedEvent(Key pubKey) {
        this.userKey = pubKey;
    }

    public Key getPublicKey(){
        return userKey;
    }
}
