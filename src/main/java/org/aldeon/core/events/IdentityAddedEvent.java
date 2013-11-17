package org.aldeon.core.events;

import org.aldeon.events.Event;
import org.aldeon.model.Identifier;

/**
 * Event triggered when user adds new Identity
 */
public class IdentityAddedEvent implements Event {
    private final Identifier userId;    //?

    public IdentityAddedEvent(Identifier userId) {
        this.userId = userId;
    }

    public Identifier getIdentifier(){
        return userId;
    }
}
