package org.aldeon.sync.procedures;

import org.aldeon.communication.Sender;
import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.db.Db;
import org.aldeon.events.ACB;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;
import org.aldeon.sync.Slot;
import org.aldeon.sync.SlotState;
import org.aldeon.sync.SlotStateUpgradeProcedure;
import org.aldeon.sync.tasks.BranchSyncTask;

import java.util.concurrent.Executor;

public class SynchronisationProcedure implements SlotStateUpgradeProcedure {

    public static final int RETRIES = 3;

    @Override
    public <T extends PeerAddress> void call(final Slot<T> slot, final Identifier topicId) {

        Core core = CoreModule.getInstance();

        final Sender<T> sender = core.getSender(slot.getAddressType());
        final Executor executor = core.clientSideExecutor();
        final Db storage = core.getStorage();

        storage.getClock(new ACB<Long>(executor) {
            @Override
            protected void react(final Long clockValue) {
               if(clockValue == null) {
                    downgrade(slot);
                    slot.setInProgress(false);
               } else {
                   retry(RETRIES, topicId, sender, slot.getPeerAddress(), storage, executor, new Callback<Boolean>() {
                       @Override
                       public void call(Boolean success) {
                           if (success) {
                               slot.setSlotState(SlotState.IN_SYNC_TIMEOUT);
                               slot.setClock(clockValue);
                               slot.setLastUpdated(0);
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

    private <T extends PeerAddress> void retry(final int tries, final Identifier topic, final Sender<T> sender, final T peer, final Db storage, final Executor executor, final Callback<Boolean> success) {

        if(tries == 0) {
            success.call(false);
        } else {
            storage.getMessageXorById(topic, new ACB<Identifier>(executor) {
                @Override
                protected void react(Identifier xor) {
                    xor = (xor == null) ? Identifier.empty() : xor;

                    sender.addTask(new BranchSyncTask<>(peer, topic, false, xor, sender, executor, storage, new Callback<Boolean>() {
                        @Override
                        public void call(Boolean syncCompleted) {

                            if(syncCompleted) {
                                success.call(true);
                            } else {
                                retry(tries - 1, topic, sender, peer, storage, executor, success);
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
}
