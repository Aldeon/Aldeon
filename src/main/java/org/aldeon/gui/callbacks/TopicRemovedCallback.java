package org.aldeon.gui.callbacks;

import org.aldeon.core.events.TopicRemovedEvent;
import org.aldeon.events.Callback;

public class TopicRemovedCallback implements Callback<TopicRemovedEvent> {

    // TopicState removed -> delete root + all children

    @Override
    public void call(TopicRemovedEvent event) {
        // Delete topic + go back if currently browsing this topic
        System.out.println("Topic removed");
    }
}
