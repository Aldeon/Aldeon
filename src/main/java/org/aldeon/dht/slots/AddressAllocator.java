package org.aldeon.dht.slots;

import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;
import org.aldeon.utils.various.Provider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AddressAllocator {

    private Map<Identifier, AddressAllocationLine> lines;

    public AddressAllocator() {
        lines = new HashMap<>();
    }

    private AddressAllocationLine getLine(Identifier topic) {
        AddressAllocationLine line = lines.get(topic);

        if(line == null) {
            line = new AddressAllocationLine();
            lines.put(topic, line);
        }

        return line;
    }

    private void cleanLine(Identifier topic) {
        AddressAllocationLine line = lines.get(topic);

        if(line != null) {
            if(line.isCompletelyEmpty()) {
                lines.remove(line);
            }
        }
    }

    public void addAddress(Identifier topic, PeerAddress address) {
        getLine(topic).addAddress(address);
        cleanLine(topic);
    }

    public void delAddress(Identifier topic, PeerAddress address) {
        getLine(topic).delAddress(address);
        cleanLine(topic);
    }

    /**
     * Ineffective implementation
     * @param address
     */
    @Deprecated
    public void delAddressFromAllLines(PeerAddress address) {
        for(AddressAllocationLine line: lines.values()) {
            line.delAddress(address);
        }
    }

    public void addSlot(Identifier topic, Callback<PeerAddress> slot) {
        getLine(topic).addSlot(slot);
        cleanLine(topic);
    }

    public void delSlot(Identifier topic, Callback<PeerAddress> slot) {
        getLine(topic).delSlot(slot);
        cleanLine(topic);
    }

    public Set<PeerAddress> getPeers(Identifier topic, int maxResults) {
        Set<PeerAddress> result = getLine(topic).getAddresses(maxResults);
        cleanLine(topic);
        return result;
    }

    public Provider<Integer> getDemandProvider(final Identifier topic) {
        return new Provider<Integer>() {
            @Override
            public Integer get() {
                AddressAllocationLine line = lines.get(topic);
                if(line == null) {
                    return 0;
                } else {
                    return line.getDemand();
                }
            }
        };
    }

}
