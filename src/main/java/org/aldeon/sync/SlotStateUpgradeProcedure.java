package org.aldeon.sync;

import org.aldeon.model.Identifier;

public interface SlotStateUpgradeProcedure {

    void handle(Slot slot, Identifier topicId);
}
