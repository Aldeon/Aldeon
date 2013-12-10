package org.aldeon.sync.procedures;

import org.aldeon.networking.common.Sender;
import org.aldeon.networking.common.OutboundRequestTask;
import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.sync.Slot;
import org.aldeon.sync.SlotState;
import org.aldeon.sync.SlotStateUpgradeProcedure;
import org.aldeon.sync.tasks.GetDiffTask;
import org.aldeon.utils.various.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Downloads the delta and updates the clock accordingly.
 */
public class DeltaDownloadingProcedure implements SlotStateUpgradeProcedure {

    private static final Logger log = LoggerFactory.getLogger(DeltaDownloadingProcedure.class);

    private final Provider<Long> timeProvider;

    public DeltaDownloadingProcedure(Provider<Long> timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public void handle(final Slot slot, Identifier topicId) {

        log.info("Attempting to download a delta");

        Callback<Long> onCompleted = new Callback<Long>() {
            @Override
            public void call(Long newClock) {
                if(newClock == null) {
                    log.info("Failed to download a delta. Revert to synchronization");
                    // Failed to download the complete delta, revert to synchronization
                    slot.setSlotState(SlotState.SYNC_IN_PROGRESS);
                } else {
                    // Successfully downloaded all new messages in delta
                    log.info("Delta downloaded.");
                    slot.setLastUpdated(timeProvider.get());
                    slot.setClock(newClock);
                    slot.setSlotState(SlotState.IN_SYNC_ON_TIME);
                }

                slot.setInProgress(false);
            }
        };

        Core core = CoreModule.getInstance();
        Sender sender = core.getSender();

        OutboundRequestTask task = new GetDiffTask(slot.getPeerAddress(), topicId, slot.getClock(), core.getStorage(), sender, onCompleted);

        sender.addTask(task);
    }
}
