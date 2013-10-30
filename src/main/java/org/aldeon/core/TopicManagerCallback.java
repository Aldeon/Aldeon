package org.aldeon.core;

import org.aldeon.common.core.Core;
import org.aldeon.events.TopicAddedEvent;
import org.aldeon.common.events.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This callback is triggered when a new topic is added by the user.
 */
public class TopicManagerCallback implements Callback<TopicAddedEvent> {

    private static final Logger log = LoggerFactory.getLogger(TopicManagerCallback.class);

    private final Core core;

    public TopicManagerCallback(Core core) {
        this.core = core;
    }

    @Override
    public void call(TopicAddedEvent evt) {

        log.info("Whoa! User just subscribed to topic " + evt.getTopicIdentifier() + "!");

        core.clientSideExecutor().execute(new Runnable() {
            @Override
            public void run() {

                // We should do something about this.

            }
        });

    }
}
