package org.aldeon.core.events;

import org.aldeon.events.Event;
import org.aldeon.model.Identifier;

public class RemoveMessageEvent implements Event {
    private final Identifier msgId;

    public RemoveMessageEvent(Identifier msgId) {
        this.msgId = msgId;
    }

    public Identifier getIdentifier(){
        return msgId;
    }

}
