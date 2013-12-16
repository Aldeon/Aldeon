package org.aldeon.dht2.interest.orders;

import org.aldeon.networking.common.PeerAddress;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TopicOrderLineImpl implements TopicOrderLine {

    private Set<PeerAddress> unassigned = new HashSet<>();
    private Set<Order> notExecutedOrders = new HashSet<>();
    private Set<Entry> executedOrders = new HashSet<>();

    @Override
    public void addAddress(PeerAddress address) {
        if(!addressKnown(address)) {
            if(notExecutedOrders.isEmpty()) {
                unassigned.add(address);
            } else {
                execute(pop(notExecutedOrders), address);
            }
        }
    }

    @Override
    public void delAddress(PeerAddress address) {
        if(unassigned.contains(address)) {
            unassigned.remove(address);
        } else {
            Entry entry = findEntryByAddress(address);
            if(entry != null) {
                entry.renewable = false;
            }
        }
    }

    @Override
    public void addOrder(Order order) {
        if(unassigned.isEmpty()) {
            notExecutedOrders.add(order);
        } else {
            execute(order, pop(unassigned));
        }
    }

    @Override
    public void delOrder(Order order) {
        Entry entry = findEntryByOrder(order);
        if(entry != null) {
            executedOrders.remove(entry);
            if(entry.renewable) {
                addAddress(entry.address);
            }
        }
    }

    @Override
    public int getDemand() {
        return notExecutedOrders.size();
    }

    @Override
    public boolean isEmpty() {
        return unassigned.isEmpty() && notExecutedOrders.isEmpty() && executedOrders.isEmpty();
    }

    @Override
    public Set<PeerAddress> getAddresses(int maxResults) {
        Set<PeerAddress> result = new HashSet<>();

        Iterator<PeerAddress> it = unassigned.iterator();

        for(int i = 0; i < maxResults; ++i) {
            if(it.hasNext()) {
                result.add(it.next());
            } else break;
        }

        Iterator<Entry> eit = executedOrders.iterator();

        for(int i = result.size(); i < maxResults; ++i) {
            if(eit.hasNext()) {
                result.add(eit.next().address);
            } else break;
        }

        return result;
    }

    private boolean addressKnown(PeerAddress address) {
        return unassigned.contains(address) || findEntryByAddress(address) != null;
    }

    private void execute(Order order, PeerAddress address) {
        Entry e = new Entry();
        e.order = order;
        e.address = address;
        executedOrders.add(e);
        order.callback().call(address);
    }

    private Entry findEntryByAddress(PeerAddress address) {
        for(Entry e: executedOrders) {
            if(e.address.equals(address)) {
                return e;
            }
        }
        return null;
    }

    private Entry findEntryByOrder(Order order) {
        for(Entry e: executedOrders) {
            if(e.order.equals(order)) {
                return e;
            }
        }
        return null;
    }

    private <T> T pop(Set<T> set) {
        Iterator<T> it = set.iterator();
        T element = it.next();
        it.remove();
        return element;
    }

    private static class Entry {
        Order order;
        PeerAddress address;
        boolean renewable = true;
    }
}
