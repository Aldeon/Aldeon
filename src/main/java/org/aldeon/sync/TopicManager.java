package org.aldeon.sync;

import org.aldeon.model.Identifier;

import java.util.Set;

/**
 * Holds the states of topics the user is interested in.
 */
public interface TopicManager {

    /**
     * Registers user's interest in a particular topic
     * @param topicId topic in which the user wishes to participate in.
     * @param slots requested number of
     */
    void addTopic(Identifier topicId, int slots);
    void delTopic(Identifier topicId);

    Set<TopicState> getTopicStates();
}
