package org.aldeon.sync.procedures;

import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.dht.Dht;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.sync.Slot;
import org.aldeon.sync.SlotState;
import org.aldeon.sync.SlotStateUpgradeProcedure;

/**
 * Upgrades the slot from EMPTY to SYNC_IN_PROGRESS.
 *
 * To achieve this, a bounty is registered in appropriate dht
 */
public class PeerFindingProcedure implements SlotStateUpgradeProcedure {

    @Override
    public void handle(final Slot slot, final Identifier topicId) {

        Core core = CoreModule.getInstance();
        final Dht dht = core.getDht(slot.getAddressType());

        Callback<PeerAddress> callback = new Callback<PeerAddress>() {
            @Override
            public void call(PeerAddress peerAddress) {

                final Callback<PeerAddress> cb = this;
                Runnable revoke = new Runnable() {
                    @Override
                    public void run() {
                        dht.delBounty(topicId, cb);
                    }
                };

                if(slot.getAddressType() == peerAddress.getType()) {
                    slot.setPeerAddress(peerAddress);
                    slot.onRevoke(revoke);
                    slot.setSlotState(SlotState.SYNC_IN_PROGRESS);
                } else {
                    revoke.run();
                }

                slot.setInProgress(false);
            }
        };

        dht.addBounty(topicId, callback);
    }
}
