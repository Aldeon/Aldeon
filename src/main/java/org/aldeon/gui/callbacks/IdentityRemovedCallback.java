package org.aldeon.gui.callbacks;

import org.aldeon.core.events.IdentityRemovedEvent;
import org.aldeon.events.Callback;

public class IdentityRemovedCallback implements Callback<IdentityRemovedEvent> {

    // Identity removed -> remove it from GUI

    @Override
    public void call(IdentityRemovedEvent event) {
        // Delete identity from GUI
        System.out.println("Removed identity");
    }
}
