package org.aldeon.gui2.various;

import javafx.event.Event;
import javafx.event.EventType;
import org.aldeon.model.Identity;

public class IdentityEvent extends Event {

    private Identity identity;

    public IdentityEvent(Identity identity) {
        super(new EventType<IdentityEvent>());
        this.identity = identity;
    }

    public Identity identity() {
        return identity;
    }
}