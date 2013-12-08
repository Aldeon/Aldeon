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
import org.aldeon.utils.various.Provider;

public class SynchronizationProcedure implements SlotStateUpgradeProcedure {

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
                public void call(Identifier xor) {
                    xor = (xor == null) ? Identifier.empty() : xor;

                    sender.addTask(new BranchSyncTask(peer, topic, false, xor, sender, storage, new Callback<Boolean>() {
                        @Override
                        public void call(Boolean syncCompleted) {

                            if(syncCompleted) {
                                success.call(true);
                            } else {
                                retry(tries - 1, topic, sender, peer, storage, success);
                            }
                        }
                    }));

                }
            });
        }
    }

    private void downgrade(Slot slot) {
        slot.setSlotState(SlotState.EMPTY);
        slot.getRevoke().run();
    }

    @Override
    public void handle(final Slot slot, final Identifier topic) {

        Core core = CoreModule.getInstance();

        final Sender sender = core.getSender();
        final Db storage = core.getStorage();

        storage.getClock(new Callback<Long>() {
            @Override
            public void call(final Long clockValue) {
                if (clockValue == null) {
                    downgrade(slot);
                    slot.setInProgress(false);
                } else {
                    retry(RETRIES, topic, sender, slot.getPeerAddress(), storage, new Callback<Boolean>() {
                        @Override
                        public void call(Boolean success) {
                            if (success) {
                                slot.setSlotState(SlotState.IN_SYNC_TIMEOUT);
                                slot.setClock(clockValue);
                                slot.setLastUpdated(timeProvider.get());
                            } else {
                                downgrade(slot);
                            }
                            slot.setInProgress(false);
                        }
                    });
                }
            }
        });
    }
}
