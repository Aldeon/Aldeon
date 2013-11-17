package org.aldeon.core.events;

import org.aldeon.events.Event;
import org.aldeon.model.Identifier;

/**
 * Event triggered when user deletes one of identites
 */
public class IdentityRemovedEvent implements Event {
    private final Identifier userId;    //?

    public IdentityRemovedEvent(Identifier userId) {
        this.userId = userId;
    }

    public Identifier getIdentifier(){
        return userId;
    }
}
