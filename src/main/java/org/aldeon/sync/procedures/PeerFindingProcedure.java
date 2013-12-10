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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Upgrades the slot from EMPTY to SYNC_IN_PROGRESS.
 *
 * To achieve this, a bounty is registered in appropriate dht
 */
public class PeerFindingProcedure implements SlotStateUpgradeProcedure {

    private static final Logger log = LoggerFactory.getLogger(PeerFindingProcedure.class);

    @Override
    public void handle(final Slot slot, final Identifier topicId) {

        log.info("Finding peers for topic " + topicId);

        Core core = CoreModule.getInstance();
        final Dht dht = core.getDht(slot.getAddressType());

        Callback<PeerAddress> callback = new Callback<PeerAddress>() {
            @Override
            public void call(final PeerAddress peerAddress) {

                log.info("Found peer for slot: " + peerAddress);

                final Callback<PeerAddress> cb = this;
                Runnable revoke = new Runnable() {
                    @Override
                    public void run() {
                        log.info("Revoking address " + peerAddress);
                        dht.delBounty(topicId, cb);
                    }
                };

                if(slot.getAddressType().equals(peerAddress.getType())) {
                    log.info("Peer type is right. Assigned to slot");
                    slot.setPeerAddress(peerAddress);
                    slot.onRevoke(revoke);
                    slot.setSlotState(SlotState.SYNC_IN_PROGRESS);
                } else {
                    log.info("Wrong peer type - expected " + slot.getAddressType());
                    revoke.run();
                }

                slot.setInProgress(false);
            }
        };

        log.info("Assigning bounty");
        dht.addBounty(topicId, callback);
    }
}
