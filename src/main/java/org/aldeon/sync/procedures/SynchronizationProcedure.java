package org.aldeon.sync.procedures;

import org.aldeon.networking.common.Sender;
import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.db.Db;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.sync.Slot;
import org.aldeon.sync.SlotState;
import org.aldeon.sync.SlotStateUpgradeProcedure;
import org.aldeon.sync.tasks.BranchSyncTask;
import org.aldeon.sync.tasks.DownloadMessageTask;
import org.aldeon.sync.tasks.GetClockTask;
import org.aldeon.utils.various.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SynchronizationProcedure implements SlotStateUpgradeProcedure {

    private static final Logger log = LoggerFactory.getLogger(SynchronizationProcedure.class);

    public static final int RETRIES = 3;
    private final Provider<Long> timeProvider;

    public SynchronizationProcedure(Provider<Long> timeProvider) {
        this.timeProvider = timeProvider;
    }

    private void retry(final int tries, final Identifier topic, final Sender sender, final PeerAddress peer, final Db storage, final Callback<Boolean> success) {

        if(tries == 0) {
            success.call(false);
        } else {
            storage.getMessageXorById(topic, new Callback<Identifier>() {
                @Override
                public void call(final Identifier xor) {

                    if(xor == null) {
                        log.info("We do not have the topic message - download it");
                        sender.addTask(new DownloadMessageTask(peer, topic, null, false, storage, new Callback<Boolean>() {
                            @Override
                            public void call(Boolean topicMessageDownloaded) {
                                if (topicMessageDownloaded) {
                                    log.info("Topic message downloaded. Continuing with synchronization");
                                    retry(tries, topic, sender, peer, storage, success);
                                } else {
                                    log.info("Peer does not have the topic message, do not bother retrying");
                                    success.call(false);
                                }
                            }
                        }));
                    } else {
                        log.info("Trying to sync topic " + topic + " with xor " + xor);
                        sender.addTask(new BranchSyncTask(peer, topic, false, xor, sender, storage, new Callback<Boolean>() {
                            @Override
                            public void call(Boolean syncCompleted) {
                                if(syncCompleted) {
                                    success.call(true);
                                } else {
                                    log.info("Sync failed. Retries left: " + tries);
                                    retry(tries - 1, topic, sender, peer, storage, success);
                                }
                            }
                        }));
                    }
                }
            });
        }
    }

    private void downgrade(Slot slot) {
        log.info("Going back to empty slot state");
        slot.setSlotState(SlotState.EMPTY);
        slot.getRevoke().run();
    }

    @Override
    public void handle(final Slot slot, final Identifier topic) {

        log.info("Synchronizing with peer " + slot.getPeerAddress());

        Core core = CoreModule.getInstance();

        final Sender sender = core.getSender();
        final Db storage = core.getStorage();

        sender.addTask(new GetClockTask(slot.getPeerAddress(), topic, new Callback<Long>() {
            @Override
            public void call(final Long clockValue) {
                if (clockValue == null) {
                    log.info("Null clock value - disconnecting with peer");
                    downgrade(slot);
                    slot.setInProgress(false);
                } else {
                    log.info("Clock value: " + clockValue);
                    retry(RETRIES, topic, sender, slot.getPeerAddress(), storage, new Callback<Boolean>() {
                        @Override
                        public void call(Boolean success) { if (success) {
                                log.info("Synchronization succeeded");
                                slot.setSlotState(SlotState.IN_SYNC_TIMEOUT);
                                slot.setClock(clockValue);
                                slot.setLastUpdated(timeProvider.get());
                            } else {
                            log.info("Synchronization failed");
                                downgrade(slot);
                            }
                            slot.setInProgress(false);
                        }
                    });
                }
            }
        }));
    }
}
