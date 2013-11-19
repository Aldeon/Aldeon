package org.aldeon.sync.procedures;

import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.dht.Bounty;
import org.aldeon.dht.Dht;
import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;
import org.aldeon.sync.Slot;
import org.aldeon.sync.SlotState;
import org.aldeon.sync.SlotStateUpgradeProcedure;

public class PeerFindingProcedure implements SlotStateUpgradeProcedure {

    @Override
    public <T extends PeerAddress> void call(final Slot<T> slot, final Identifier topicId) {

        slot.setInProgress(true);

        Core core = CoreModule.getInstance();

        Dht<T> dht = core.getDht(slot.getAddressType());

        if(dht == null) {
            throw new IllegalArgumentException("WTF");
        } else {

            Bounty<T> bounty = new Bounty<T>() {
                @Override
                public Identifier getIdentifier() {
                    return topicId;
                }

                @Override
                public void onPeerFound(T address) {
                    peerFound(address, slot);
                }
            };

            dht.addBounty(bounty);
        }

    }

    private <T extends PeerAddress> void peerFound(T address, Slot<T> slot) {

        slot.setPeerAddress(address);
        slot.setSlotState(SlotState.SYNC_IN_PROGRESS);
        slot.setInProgress(false);

    }
}
