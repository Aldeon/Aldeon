package org.aldeon.core.events;

import org.aldeon.events.Event;
import org.aldeon.model.User;

public class UserChangedEvent implements Event {

    private final User usr;

    public UserChangedEvent(User usr) {
        this.usr = usr;
    }

    public User getUser() {
        return usr;
    }
}
