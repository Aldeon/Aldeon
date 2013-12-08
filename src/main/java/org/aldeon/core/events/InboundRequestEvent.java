package org.aldeon.core.events;

import org.aldeon.networking.common.InboundRequestTask;
import org.aldeon.events.Event;

public class InboundRequestEvent implements Event {

    private final InboundRequestTask task;

    public InboundRequestEvent(InboundRequestTask task) {
        this.task = task;
    }

    public InboundRequestTask getTask() {
        return task;
    }
}
