package org.aldeon.core.events;

import org.aldeon.events.Event;
import org.aldeon.model.Identifier;
import org.aldeon.model.Identity;

/**
 * Event triggered when user adds new Identity
 */
public class IdentityAddedEvent implements Event {
    private final Identity userId;    //?

    public IdentityAddedEvent(Identity user) {
        this.userId = user;
    }

    public Identity getIdentity(){
        return userId;
    }
}
