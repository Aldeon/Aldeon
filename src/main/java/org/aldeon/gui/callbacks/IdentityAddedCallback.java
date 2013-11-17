package org.aldeon.gui.callbacks;

import org.aldeon.core.events.IdentityAddedEvent;
import org.aldeon.events.Callback;

public class IdentityAddedCallback implements Callback<IdentityAddedEvent> {

    // Identity added -> show it in GUI

    @Override
    public void call(IdentityAddedEvent event) {
        // Inform GUI about new id
        System.out.println("New identity");
    }
}
