package org.aldeon.sync.procedures;


import org.aldeon.config.Config;
import org.aldeon.model.Identifier;
import org.aldeon.sync.Slot;
import org.aldeon.sync.SlotState;
import org.aldeon.sync.SlotStateUpgradeProcedure;
import org.aldeon.utils.various.Provider;

public class CheckTimeoutProcedure implements SlotStateUpgradeProcedure{

    private final Provider<Long> timeProvider;

    public CheckTimeoutProcedure(Provider<Long> timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public void handle(Slot slot, Identifier topic) {
        int timeout = Config.config().getInt("sync.intervals.diff");
        if(slot.getLastUpdated() + timeout < timeProvider.get()) {
            slot.setSlotState(SlotState.IN_SYNC_TIMEOUT);
        }
        slot.setInProgress(false);
    }
}
