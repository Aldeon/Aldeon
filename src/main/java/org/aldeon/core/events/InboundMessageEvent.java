package org.aldeon.core.events;

import org.aldeon.communication.task.InboundRequestTask;
import org.aldeon.events.Event;
import org.aldeon.model.Message;


public class InboundMessageEvent implements Event {
    private final Message msg;

    public InboundMessageEvent(Message msg) {
        this.msg = msg;
    }

    public Message getMessage(){
        return msg;
    }
}
