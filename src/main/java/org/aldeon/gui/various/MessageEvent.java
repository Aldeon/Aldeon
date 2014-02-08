package org.aldeon.gui.various;

import javafx.event.Event;
import javafx.event.EventType;
import org.aldeon.model.Message;

public class MessageEvent extends Event{
    private final Message message;

    public MessageEvent(Message message) {
        super(new EventType<MessageEvent>());
        this.message = message;
    }

    public Message message() {
        return message;
    }
}
