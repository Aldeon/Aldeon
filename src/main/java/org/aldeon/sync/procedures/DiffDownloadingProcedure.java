package org.aldeon.sync.procedures;

import org.aldeon.db.Db;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.Sender;
import org.aldeon.protocol.response.DiffResponse;
import org.aldeon.sync.Slot;
import org.aldeon.sync.SlotState;
import org.aldeon.sync.SlotStateUpgradeProcedure;
import org.aldeon.sync.tasks.DiffResult;
import org.aldeon.sync.tasks.GetDiffTask;
import org.aldeon.utils.various.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DiffDownloadingProcedure implements SlotStateUpgradeProcedure {

    private static final Logger log = LoggerFactory.getLogger(DiffDownloadingProcedure.class);

    private final Sender sender;
    private final Db db;
    private final Provider<Long> timeProvider;

    public DiffDownloadingProcedure(Db db, Sender sender, Provider<Long> timeProvider) {
        this.sender = sender;
        this.db = db;
        this.timeProvider = timeProvider;
    }

    @Override
    public void handle(final Slot slot, Identifier topic) {

        log.info("Downloading diff for topic" + topic + " from peer " + slot.getPeerAddress());

        sender.addTask(new GetDiffTask(slot.getPeerAddress(), topic, slot.getClock(), db, sender, new Callback<DiffResult>() {
            @Override
            public void call(DiffResult diffDownloadResult) {
                if(successful(diffDownloadResult)) {
                    slot.setClock(diffDownloadResult.clock);
                    slot.setLastUpdated(timeProvider.get());
                    slot.setSlotState(SlotState.IN_SYNC_ON_TIME);
                    log.info("Diff applied successfully. New clock: " + diffDownloadResult.clock + ", Messages downloaded: " + diffDownloadResult.messagesDownloaded);

                } else {
                    slot.setSlotState(SlotState.SYNC_IN_PROGRESS);
                    log.info("Diff failed.");
                }
                slot.setInProgress(false);
            }
        }));

    }

    private boolean successful(DiffResult result) {
        return result.clock != null && result.failedRequests == 0;
    }
}
