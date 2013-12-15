package org.aldeon.sync.procedures;

import org.aldeon.db.Db;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.Sender;
import org.aldeon.sync.Slot;
import org.aldeon.sync.SlotState;
import org.aldeon.sync.SlotStateUpgradeProcedure;
import org.aldeon.sync.tasks.BranchSyncTask;
import org.aldeon.sync.tasks.DownloadMessageTask;
import org.aldeon.sync.tasks.GetClockTask;
import org.aldeon.sync.tasks.SyncResult;
import org.aldeon.utils.various.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SynchronizationProcedure implements SlotStateUpgradeProcedure {

    private static final Logger log = LoggerFactory.getLogger(SynchronizationProcedure.class);

    private final Db db;
    private final Sender sender;
    private final Reducer<SyncResult> reducer;

    public SynchronizationProcedure(Db db, Sender sender) {
        this.db = db;
        this.sender = sender;
        this.reducer = new SyncResult.SyncResultReducer();
    }

    @Override
    public void handle(final Slot slot, final Identifier topic) {
        /*
            We need to synchronize with a specified server.
            In case of failure, retry at most 3 times.
         */

        log.info("Synchronizing topic" + topic + " with peer " + slot.getPeerAddress());

        // TODO: implement the retry system

        fetchClock(slot, topic, new Callback<Boolean>() {
            @Override
            public void call(Boolean clockFetched) {

                if (clockFetched) {
                    synchronize(slot.getPeerAddress(), topic, true, new Callback<SyncResult>() {
                        @Override
                        public void call(SyncResult syncResult) {
                            if (successful(syncResult)) {
                                log.info("Synchronization successful. Downloaded: " + syncResult.messagesDownloaded
                                        + ", suggested: " + syncResult.messagesSuggested
                                        + ", accidental errors: " + syncResult.accidentalErrors
                                        + ", purposeful errors: " + syncResult.purposefulErrors
                                        + ", failed requests: " + syncResult.failedRequests);
                                slot.setSlotState(SlotState.IN_SYNC_TIMEOUT);
                            } else {
                                log.info("Synchronization failed. Downloaded: " + syncResult.messagesDownloaded
                                        + ", suggested: " + syncResult.messagesSuggested
                                        + ", accidental errors: " + syncResult.accidentalErrors
                                        + ", purposeful errors: " + syncResult.purposefulErrors
                                        + ", failed requests: " + syncResult.failedRequests);
                                slot.setSlotState(SlotState.EMPTY);
                            }
                            slot.setInProgress(false);
                        }
                    });
                } else {
                    log.info("Failed to fetch clock.");
                    slot.setSlotState(SlotState.EMPTY);
                    slot.setInProgress(false);
                }
            }
        });
    }

    private void fetchClock(final Slot slot, Identifier topic, final Callback<Boolean> onOperationCompleted) {
        sender.addTask(new GetClockTask(slot.getPeerAddress(), topic, new Callback<Long>() {
            @Override
            public void call(Long val) {
                if(val == null) {
                    onOperationCompleted.call(false);
                } else {
                    slot.setClock(val);
                    onOperationCompleted.call(true);
                }
            }
        }));
    }

    private boolean successful(SyncResult result) {
        return result.failedRequests == 0 && result.purposefulErrors == 0;
    }

    private void synchronize(final PeerAddress peer, final Identifier branch, final boolean allowDownload, final Callback<SyncResult> onOperationCompleted) {
        db.getMessageXorById(branch, new Callback<Identifier>() {
            @Override
            public void call(Identifier xor) {
                if(xor == null) {
                    // Whoops, we need to download the topic first.
                    // the boolean flag alleviates the possible threat of uncontrolled recursion
                    if(allowDownload) {
                        downloadAndThenSynchronize(peer, branch, onOperationCompleted);
                    } else {
                        onOperationCompleted.call(SyncResult.requestFailed());
                    }
                } else {
                    sender.addTask(new BranchSyncTask(peer, branch, xor, false, sender, db, onOperationCompleted));
                }
            }
        });
    }

    private void downloadAndThenSynchronize(final PeerAddress peer, final Identifier branch, final Callback<SyncResult> onOperationCompleted) {
        sender.addTask(new DownloadMessageTask(peer, branch, Identifier.empty(), false, db, new Callback<DownloadMessageTask.Result>() {
            @Override
            public void call(DownloadMessageTask.Result downloadResult) {
                switch(downloadResult) {
                    case MESSAGE_INSERTED:
                        // Great, proceed.
                        synchronize(peer, branch, false, new Callback<SyncResult>() {
                            @Override
                            public void call(SyncResult val) {
                                onOperationCompleted.call(reducer.reduce(val, SyncResult.messageDownloaded()));
                            }
                        });
                        break;

                    case MESSAGE_EXISTS:
                        // We must have downloaded it from someone else in the meantime. Proceed.
                        synchronize(peer, branch, false, onOperationCompleted);
                        break;

                    case MESSAGE_NOT_ON_SERVER:
                        // Server must have removed the topic, so synchronization is pointless.
                        onOperationCompleted.call(SyncResult.requestFailed());
                        break;

                    case PARENT_UNKNOWN:
                        // Impossible
                        onOperationCompleted.call(SyncResult.requestFailed());
                        break;

                    case COMMUNICATION_ERROR:
                        // Communication failed
                        onOperationCompleted.call(SyncResult.requestFailed());
                        break;
                }
            }
        }));
    }
}
