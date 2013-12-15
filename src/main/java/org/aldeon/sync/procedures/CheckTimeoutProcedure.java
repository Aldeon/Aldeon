package org.aldeon.sync.procedures;


import org.aldeon.model.Identifier;
import org.aldeon.sync.Slot;
import org.aldeon.sync.SlotState;
import org.aldeon.sync.SlotStateUpgradeProcedure;
import org.aldeon.utils.various.Provider;

public class CheckTimeoutProcedure implements SlotStateUpgradeProcedure{

    private static final long TIMEOUT = 15000;

    private final Provider<Long> timeProvider;

    public CheckTimeoutProcedure(Provider<Long> timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public void handle(Slot slot, Identifier topic) {
        if(slot.getLastUpdated() + TIMEOUT < timeProvider.get()) {
            slot.setSlotState(SlotState.IN_SYNC_TIMEOUT);
        }
        slot.setInProgress(false);
    }
}
