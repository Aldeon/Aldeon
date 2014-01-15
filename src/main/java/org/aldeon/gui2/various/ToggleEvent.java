package org.aldeon.gui2.various;

import javafx.event.Event;
import javafx.event.EventType;

public class ToggleEvent extends Event {
    private final boolean toggle;

    public ToggleEvent(boolean toggle) {
        super(new EventType<ToggleEvent>());
        this.toggle = toggle;
    }

    public boolean getToggle() {
        return toggle;
    }
}
