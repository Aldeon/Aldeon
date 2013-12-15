package org.aldeon.sync.procedures;


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

public class PeerFindingProcedure implements SlotStateUpgradeProcedure {

    private static final Logger log = LoggerFactory.getLogger(PeerFindingProcedure.class);

    @Override
    public void handle(final Slot slot, final Identifier topic) {

        log.info("Looking for peer interested in topic " + topic);

        // 1. Fetch the appropriate DHT
        Dht dht = CoreModule.getInstance().getDht(slot.getAddressType());

        // 2. Unregister last used address, if such address exists
        if(slot.getPeerAddress() != null) {
            dht.delBounty(topic,  slot.getBountyHandler());
            slot.setPeerAddress(null);
            slot.setBountyHandler(null);
        }

        // 3. Create appropriate handler
        Callback<PeerAddress> handler = new Callback<PeerAddress>() {
            @Override
            public void call(PeerAddress peer) {
                log.info("Peer (" + peer + ") interested in topic " + topic + " found and assigned to slot.");
                slot.setPeerAddress(peer);
                slot.setSlotState(SlotState.SYNC_IN_PROGRESS);
                slot.setInProgress(false);
            }
        };

        // 4. Register demand for a new address
        slot.setBountyHandler(handler);
        dht.addBounty(topic, handler);
    }
}
