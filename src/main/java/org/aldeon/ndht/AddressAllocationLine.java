package org.aldeon.ndht;

import org.aldeon.events.Callback;
import org.aldeon.net.PeerAddress;

import java.util.HashSet;
import java.util.Set;

public class AddressAllocationLine<T extends PeerAddress> {

    private class Entry {
        public Callback<T> callback;
        public T address;
        public boolean renewable = true;
    }


    private final Set<T> unassigned;
    private final Set<Callback<T>> emptySlots;
    private final Set<Entry> assignedSlots;

    public AddressAllocationLine() {
        unassigned = new HashSet<>();
        emptySlots = new HashSet<>();
        assignedSlots = new HashSet<>();
    }

    public void addAddress(T address) {

        if(!addressKnown(address))  {
            if(emptySlots.isEmpty()) {
                unassigned.add(address);
            } else {
                Callback<T> slot = emptySlots.iterator().next();
                emptySlots.remove(slot);

                addSlotAndTrigger(slot, address);
            }
        }
    }

    public void delAddress(T address) {

        if(unassigned.contains(address)) {
            unassigned.remove(address);
        } else {
            Entry e = findEntryByAddress(address);
            if(e != null) e.renewable = false;
        }
    }

    public void addSlot(Callback<T> slot) {
        if(unassigned.isEmpty()) {
            emptySlots.add(slot);
        } else {
            T address = unassigned.iterator().next();
            unassigned.remove(address);

            addSlotAndTrigger(slot, address);
        }
    }

    public void delSlot(Callback<T> slot) {
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

    public Set<T> getAddresses(int maxResults) {
        return null;
    }

    public boolean isCompletelyEmpty() {
        return unassigned.isEmpty() && emptySlots.isEmpty() && assignedSlots.isEmpty();
    }

    private boolean addressKnown(T address) {
        if(unassigned.contains(address)) {
            return true;
        } else {
            return findEntryByAddress(address) != null;
        }
    }

    private void addSlotAndTrigger(Callback<T> slot, T address) {
        Entry e = new Entry();
        e.callback = slot;
        e.address = address;
        assignedSlots.add(e);

        // trigger slot
        slot.call(address);
    }

    private Entry findEntryBySlot(Callback<T> slot) {
        for(Entry e: assignedSlots) {
            if(e.callback.equals(slot)) {
                return e;
            }
        }
        return null;
    }

    private Entry findEntryByAddress(T address) {
        for(Entry e: assignedSlots) {
            if(e.address.equals(address)) {
                return e;
            }
        }
        return null;
    }
}
