package org.aldeon.gui.callbacks;

import org.aldeon.core.events.TopicAddedEvent;
import org.aldeon.events.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This callback is triggered when a new topic is added by the user.
 */
public class TopicAddedCallback implements Callback<TopicAddedEvent> {

    private static final Logger log = LoggerFactory.getLogger(TopicAddedCallback.class);

    @Override
    public void call(TopicAddedEvent evt) {

        log.info("Whoa! User just subscribed to topic " + evt.getTopicIdentifier() + "!");

        // We should do something about this.
    }
}
