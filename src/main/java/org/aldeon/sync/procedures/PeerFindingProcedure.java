package org.aldeon.sync.procedures;

import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.dht.Dht;
import org.aldeon.events.ACB;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;
import org.aldeon.sync.Slot;
import org.aldeon.sync.SlotState;
import org.aldeon.sync.SlotStateUpgradeProcedure;

/**
 * Upgrades the slot from EMPTY to SYNC_IN_PROGRESS.
 *
 * To achieve this, a bounty is registered in appropriate dht
 */
public class PeerFindingProcedure implements SlotStateUpgradeProcedure {

    private <T extends PeerAddress> void work(Class<T> addressType, final Slot slot, final Identifier topicId) {

        Core core = CoreModule.getInstance();
        final Dht<T> dht = core.getDht(addressType);

        Callback<T> callback = new ACB<T>(core.clientSideExecutor()) {
            @Override
            protected void react(T val) {

                final Callback<T> cb = this;

                slot.setPeerAddress(val);
                slot.setSlotState(SlotState.SYNC_IN_PROGRESS);
                slot.onRevoke(new Runnable() {
                    @Override
                    public void run() {
                        dht.delBounty(topicId, cb);
                    }
                });
                slot.setInProgress(false);
            }
        };

        dht.addBounty(topicId, callback);
    }

    @Override
    public void handle(Slot slot, Identifier topicId) {
        work(slot.getPeerAddress().getClass(), slot, topicId);
    }
}
