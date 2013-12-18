package org.aldeon.core.events;

import org.aldeon.events.Event;
import org.aldeon.model.User;

public class UserAddedEvent implements Event {

    private final User usr;

    public UserAddedEvent(User usr) {
        this.usr = usr;
    }

    public User getUser() {
        return usr;
    }
}
