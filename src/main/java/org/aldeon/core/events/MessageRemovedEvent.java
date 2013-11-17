package org.aldeon.core.events;

import org.aldeon.events.Event;
import org.aldeon.model.Identifier;

public class MessageRemovedEvent implements Event {
    private final Identifier msgId;

    public MessageRemovedEvent(Identifier msgId) {
        this.msgId = msgId;
    }

    public Identifier getIdentifier(){
        return msgId;
    }

}
