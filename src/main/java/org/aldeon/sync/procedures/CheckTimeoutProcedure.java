package org.aldeon.sync.procedures;

import org.aldeon.model.Identifier;
import org.aldeon.sync.Slot;
import org.aldeon.sync.SlotState;
import org.aldeon.sync.SlotStateUpgradeProcedure;
import org.aldeon.utils.various.Provider;

public class CheckTimeoutProcedure implements SlotStateUpgradeProcedure {

    private static final long TIMEOUT = 15;

    private final Provider<Long> timeProvider;

    public CheckTimeoutProcedure(Provider<Long> timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public void handle(Slot slot, Identifier topic) {
        long time = timeProvider.get();

        if(slot.getLastUpdated() + TIMEOUT < time) {
            slot.setSlotState(SlotState.IN_SYNC_TIMEOUT);
        }

        slot.setInProgress(false);
    }
}
