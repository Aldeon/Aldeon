package org.aldeon.sync;

import org.aldeon.model.Identifier;

import java.util.Set;

/**
 * Holds the slots responsible for updating the client's knowledge
 * about messages in a particular topic.
 */
public interface TopicState {

    /**
     * Topic id
     * @return
     */
    Identifier getIdentifier();

    /**
     * Lists all slots assigned to this topic
     * @return
     */
    Set<Slot> getSlots();

    /**
     * Assigns a new slot to this topic
     * @param slot
     */
    void addSlot(Slot slot);

    /**
     * Removes the specified slot
     * @param slot
     */
    void delSlot(Slot slot);
}
