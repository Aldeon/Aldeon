package org.aldeon.dht;

import org.aldeon.common.model.Identifier;
import org.aldeon.common.net.TemporaryPeerAddress;
import org.aldeon.utils.time.TimeProvider;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class DhtTimeoutDecorator<T extends TemporaryPeerAddress> implements Dht<T> {

    private TimeProvider timer;
    private SortedSet<TemporaryPeerAddress> timeouts;
    private Dht<T> dht;

    public DhtTimeoutDecorator(Dht<T> dht, TimeProvider timer) {
        this.timer = timer;
        timeouts = new TreeSet<>(new TimeoutComparator());
        this.dht = dht;
    }

    @Override
    public void insert(T address) {
        if(address.getTimeout() >= timer.getTime()) {
            dht.insert(address);
            timeouts.add(address);
        }
    }

    @Override
    public void remove(T address) {
        timeouts.remove(address);
        dht.remove(address);
    }

    @Override
    public Set<T> getNearest(Identifier identifier, int maxResults) {
        return dht.getNearest(identifier, maxResults);
    }

    public void refresh() {
        Set<TemporaryPeerAddress> expired = timeouts.headSet(new AddressStub(timer.getTime()));

        for(TemporaryPeerAddress address: expired) {
            expired.remove(address);
            dht.remove((T) address);
        }
    }

    private class TimeoutComparator implements Comparator<TemporaryPeerAddress> {

        @Override
        public int compare(TemporaryPeerAddress o1, TemporaryPeerAddress o2) {
            long diff = o1.getTimeout() - o2.getTimeout();
            if(diff < 0) return -1;
            if(diff > 0) return 1;
            return 0;
        }
    }

    private class AddressStub implements TemporaryPeerAddress {

        private final long timeout;

        public AddressStub(long timeout) {
            this.timeout = timeout;
        }

        @Override
        public long getTimeout() {
            return timeout;
        }

        @Override
        public Identifier getIdentifier() {
            return null;
        }
    }
}
