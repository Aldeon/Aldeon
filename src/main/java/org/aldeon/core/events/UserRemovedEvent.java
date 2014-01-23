package org.aldeon.core.events;

import org.aldeon.crypt.Key;
import org.aldeon.events.Event;
import org.aldeon.model.User;

/**
 *
 */
public class UserRemovedEvent implements Event {
    private Key publicKey;

    public UserRemovedEvent(Key publicKey) {
        this.publicKey = publicKey;
    }

    public Key publicKey() {
        return publicKey;
    }

}
