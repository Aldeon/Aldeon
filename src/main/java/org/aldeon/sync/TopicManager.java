package org.aldeon.sync;

import org.aldeon.model.Identifier;
import org.aldeon.networking.common.AddressType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Holds the states of topics the user is interested in.
 */
public class TopicManager {

    private Map<Identifier, TopicState> topics = new HashMap<>();

    /**
     * Registers user's interest in a particular topic
     * @param topic topic in which the user wishes to participate in.
     */
    public void addTopic(Identifier topic) {

        topics.put(topic, createTopicState(topic));
    }

    public void delTopic(Identifier topicId) {
        // TODO: wait until all slots are not in progress, then revoke and remove
    }

    Set<TopicState> getTopicStates() {
     return new HashSet<>(topics.values());
    }

    private TopicState createTopicState(Identifier topic) {

        TopicState state = new TopicState(topic);

        state.addSlot(new Slot(SlotType.NORMAL, new AddressType("IPV4")));

        return state;
    }
}
