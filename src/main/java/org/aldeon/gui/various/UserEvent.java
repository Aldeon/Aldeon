package org.aldeon.gui.various;

import javafx.event.Event;
import javafx.event.EventType;
import org.aldeon.model.UserImpl;

/**
 *
 */
public class UserEvent extends Event {
    private UserImpl user;
    private boolean toDelete;

    public UserEvent(UserImpl user, boolean toDelete) {
        super(new EventType<UserEvent>());
        this.user = user;
        this.toDelete = toDelete;
    }

    public UserImpl user() {
        return user;
    }

    public boolean toDelete() {
        return toDelete;
    }
}
