package org.aldeon.sync.procedures;

import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;
import org.aldeon.sync.Slot;
import org.aldeon.sync.SlotStateUpgradeProcedure;

public class SynchronisationProcedure implements SlotStateUpgradeProcedure {

    @Override
    public <T extends PeerAddress> void call(Slot<T> slot, Identifier topicId) {

    }
}
