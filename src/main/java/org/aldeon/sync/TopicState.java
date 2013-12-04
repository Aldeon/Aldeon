package org.aldeon.sync;

import org.aldeon.model.Identifier;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Holds the slots responsible for updating the client's knowledge
 * about messages in a particular topic.
 */
public class TopicState {

    private final Identifier topic;
    private final Set<Slot> slots;

    public TopicState(Identifier topic) {
        this.topic = topic;
        this.slots = new HashSet<>();
    }

    /**
     * Topic id
     * @return
     */
    Identifier getIdentifier() {
        return topic;
    }

    /**
     * Lists all slots assigned to this topic
     * @return
     */
    Set<Slot> getSlots() {
        return slots;
    }

    /**
     * Assigns a new slot to this topic
     * @param slot
     */
    void addSlot(Slot slot) {
        slots.add(slot);
    }

    /**
     * Removes the specified slot
     * @param slot
     */
    void delSlot(Slot slot) {
        slots.remove(slot);
    }
}
