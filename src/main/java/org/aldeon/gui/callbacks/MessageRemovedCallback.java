package org.aldeon.gui.callbacks;

import org.aldeon.core.events.MessageRemovedEvent;
import org.aldeon.events.Callback;

public class MessageRemovedCallback implements Callback<MessageRemovedEvent> {

    // Remove message -> remove from GUI

    @Override
    public void call(MessageRemovedEvent event) {
        // Delete message from GUI
        System.out.println("Delete message");
    }
}
