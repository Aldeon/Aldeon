package org.aldeon.core.events;

import org.aldeon.events.Event;
import org.aldeon.networking.common.InboundRequestTask;

public class InboundRequestEvent implements Event {

    private final InboundRequestTask task;

    public InboundRequestEvent(InboundRequestTask task) {
        this.task = task;
    }

    public InboundRequestTask getTask() {
        return task;
    }
}
