package org.aldeon.gui.callbacks;

import org.aldeon.core.events.MessageAddedEvent;
import org.aldeon.events.Callback;

public class MessageAddedCallback implements Callback<MessageAddedEvent> {

    // New message -> show it in GUI

    @Override
    public void call(MessageAddedEvent event) {
        // Inform GUI about new message
        System.out.println("New message: " + event.getMessage().getContent());
    }
}
