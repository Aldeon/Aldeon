package org.aldeon.core.events;

import org.aldeon.events.Event;
import org.aldeon.model.Identifier;

/**
 *  Event triggered when user removes Thread
 */
public class TopicRemovedEvent implements Event {

    private final Identifier identifier;

    public TopicRemovedEvent(Identifier identifier) {
        this.identifier = identifier;
    }

    public Identifier getTopicIdentifier() {
        return identifier;
    }
}
