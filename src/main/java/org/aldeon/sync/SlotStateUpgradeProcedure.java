package org.aldeon.sync;

import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;

public interface SlotStateUpgradeProcedure {

    <T extends PeerAddress> void call(Slot<T> slot, Identifier topicId);
}
