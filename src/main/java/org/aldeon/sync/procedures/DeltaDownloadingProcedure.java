package org.aldeon.sync.procedures;

import org.aldeon.communication.Sender;
import org.aldeon.communication.task.OutboundRequestTask;
import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.sync.Slot;
import org.aldeon.sync.SlotState;
import org.aldeon.sync.SlotStateUpgradeProcedure;
import org.aldeon.sync.tasks.GetDiffTask;
import org.aldeon.utils.various.Provider;

/**
 * Downloads the delta and updates the clock accordingly.
 */
public class DeltaDownloadingProcedure implements SlotStateUpgradeProcedure {

    private final Provider<Long> timeProvider;

    public DeltaDownloadingProcedure(Provider<Long> timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public void handle(final Slot slot, Identifier topicId) {

        Callback<Long> onCompleted = new Callback<Long>() {
            @Override
            public void call(Long newClock) {

                if(newClock == null) {
                    // Failed to download the complete delta, revert to synchronization
                    slot.setSlotState(SlotState.SYNC_IN_PROGRESS);
                } else {
                    // Successfully downloaded all new messages in delta

                    slot.setLastUpdated(timeProvider.get());
                    slot.setClock(newClock);
                    slot.setSlotState(SlotState.IN_SYNC_ON_TIME);
                }

                slot.setInProgress(false);
            }
        };

        Core core = CoreModule.getInstance();
        Sender sender = core.getSender(slot.getPeerAddress().getType());

        OutboundRequestTask task = new GetDiffTask(slot.getPeerAddress(), topicId, slot.getClock(), core.getStorage(), sender, onCompleted);

        sender.addTask(task);
    }
}
