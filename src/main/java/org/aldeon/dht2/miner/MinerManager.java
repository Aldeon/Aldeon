package org.aldeon.dht2.miner;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import org.aldeon.dht2.Dht2;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.Sender;


public class MinerManager {

    private final Dht2 dht;
    private SetMultimap<AddressType, Identifier> busy = HashMultimap.create();

    private final Sender sender;

    public MinerManager(Dht2 dht, Sender sender) {
        this.dht = dht;
        this.sender = sender;
    }

    private void notifyDemand(AddressType addressType, Identifier topic) {
        if(!isBusy(addressType, topic)) {
            work(addressType, topic);
        }
    }

    private boolean isBusy(AddressType addressType, Identifier topic) {
        return busy.containsEntry(addressType, topic);
    }

    private void work(AddressType addressType, Identifier topic) {

    }
}
