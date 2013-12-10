package org.aldeon.sync.procedures;

import org.aldeon.model.Identifier;
import org.aldeon.sync.Slot;
import org.aldeon.sync.SlotState;
import org.aldeon.sync.SlotStateUpgradeProcedure;
import org.aldeon.utils.various.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckTimeoutProcedure implements SlotStateUpgradeProcedure {

    private static final Logger log = LoggerFactory.getLogger(CheckTimeoutProcedure.class);

    private static final long TIMEOUT = 15;

    private final Provider<Long> timeProvider;

    public CheckTimeoutProcedure(Provider<Long> timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public void handle(Slot slot, Identifier topic) {
        log.info("Checking if another delta is needed");
        long time = timeProvider.get();

        if(slot.getLastUpdated() + TIMEOUT < time) {
            log.info("Delta needed for topic " + topic);
            slot.setSlotState(SlotState.IN_SYNC_TIMEOUT);
        } else {
            log.info("Delta not needed");
        }

        slot.setInProgress(false);
    }
}
