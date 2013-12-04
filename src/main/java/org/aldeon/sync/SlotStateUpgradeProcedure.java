package org.aldeon.sync;

import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;

public interface SlotStateUpgradeProcedure {

    void handle(Slot slot, Identifier topicId);
}
