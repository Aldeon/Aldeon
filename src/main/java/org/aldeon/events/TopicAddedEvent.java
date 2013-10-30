package org.aldeon.events;

import org.aldeon.common.events.Event;
import org.aldeon.common.model.Identifier;

/**
 * Event triggered when user subscribes to a new topic.
 */
public class TopicAddedEvent implements Event {

    private final Identifier identifier;

    public TopicAddedEvent(Identifier identifier) {
        this.identifier = identifier;
    }

    public Identifier getTopicIdentifier() {
        return identifier;
    }
}
