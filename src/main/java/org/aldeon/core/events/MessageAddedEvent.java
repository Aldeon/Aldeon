package org.aldeon.core.events;

import org.aldeon.events.Event;
import org.aldeon.model.Message;


public class MessageAddedEvent implements Event {
    private final Message msg;

    public MessageAddedEvent(Message msg) {
        this.msg = msg;
    }

    public Message getMessage(){
        return msg;
    }
}
