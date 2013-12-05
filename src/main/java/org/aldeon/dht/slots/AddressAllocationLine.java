package org.aldeon.dht.slots;

import org.aldeon.events.Callback;
import org.aldeon.net.PeerAddress;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AddressAllocationLine {

    private class Entry {
        public Callback<PeerAddress> callback;
        public PeerAddress address;
        public boolean renewable = true;
    }

    private final Set<PeerAddress> unassigned;
    private final Set<Callback<PeerAddress>> emptySlots;
    private final Set<Entry> assignedSlots;

    public AddressAllocationLine() {
        unassigned = new HashSet<>();
        emptySlots = new HashSet<>();
        assignedSlots = new HashSet<>();
    }

    public void addAddress(PeerAddress address) {

        if(!addressKnown(address))  {
            if(emptySlots.isEmpty()) {
                unassigned.add(address);
            } else {
                Callback<PeerAddress> slot = emptySlots.iterator().next();
                emptySlots.remove(slot);

                addSlotAndTrigger(slot, address);
            }
        }
    }

    public void delAddress(PeerAddress address) {

        if(unassigned.contains(address)) {
            unassigned.remove(address);
        } else {
            Entry e = findEntryByAddress(address);
            if(e != null) e.renewable = false;
        }
    }

    public void addSlot(Callback<PeerAddress> slot) {
        if(unassigned.isEmpty()) {
            emptySlots.add(slot);
        } else {
            PeerAddress address = unassigned.iterator().next();
            unassigned.remove(address);

            addSlotAndTrigger(slot, address);
        }
    }

    public void delSlot(Callback<PeerAddress> slot) {
        Entry e = findEntryBySlot(slot);
        if(e != null) {
            assignedSlots.remove(e);
            if(e.renewable) {
                addAddress(e.address);
            }
        }
    }

    public int getDemand() {
        return emptySlots.size();
    }

    public Set<PeerAddress> getAddresses(int maxResults) {

        Set<PeerAddress> result = new HashSet<>();

        Iterator<PeerAddress> it = unassigned.iterator();

        for(int i = 0; i < maxResults; ++i) {
            if(it.hasNext()) {
                result.add(it.next());
            } else break;
        }

        Iterator<Entry> eit = assignedSlots.iterator();

        for(int i = result.size(); i < maxResults; ++i) {
            if(eit.hasNext()) {
                result.add(eit.next().address);
            } else break;
        }

        return result;
    }

    public boolean isCompletelyEmpty() {
        return unassigned.isEmpty() && emptySlots.isEmpty() && assignedSlots.isEmpty();
    }

    private boolean addressKnown(PeerAddress address) {
        return unassigned.contains(address) || findEntryByAddress(address) != null;
    }

    private void addSlotAndTrigger(Callback<PeerAddress> slot, PeerAddress address) {
        Entry e = new Entry();
        e.callback = slot;
        e.address = address;
        assignedSlots.add(e);

        // trigger slot
        slot.call(address);
    }

    private Entry findEntryBySlot(Callback<PeerAddress> slot) {
        for(Entry e: assignedSlots) {
            if(e.callback.equals(slot)) {
                return e;
            }
        }
        return null;
    }

    private Entry findEntryByAddress(PeerAddress address) {
        for(Entry e: assignedSlots) {
            if(e.address.equals(address)) {
                return e;
            }
        }
        return null;
    }
}
