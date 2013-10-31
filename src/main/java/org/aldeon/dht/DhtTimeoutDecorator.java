package org.aldeon.dht;

import org.aldeon.model.Identifier;
import org.aldeon.net.TemporaryIdentifiablePeerAddress;
import org.aldeon.utils.time.TimeProvider;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Class enhances the existing Dht, providing a method of removing elements
 * with too low getTimeout() value. This way, each element can be put into
 * the Dht for a specific period of time.
 * @param <T>
 */
public class DhtTimeoutDecorator<T extends TemporaryIdentifiablePeerAddress> implements Dht<T> {

    private TimeProvider timer;
    private SortedSet<TemporaryIdentifiablePeerAddress> timeouts;
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

    @SuppressWarnings("unchecked")
    public void refresh() {
        Set<TemporaryIdentifiablePeerAddress> expired = timeouts.headSet(new AddressStub(timer.getTime()));

        for(TemporaryIdentifiablePeerAddress address: expired) {
            expired.remove(address);
            dht.remove((T) address);
        }
    }

    private class TimeoutComparator implements Comparator<TemporaryIdentifiablePeerAddress> {

        @Override
        public int compare(TemporaryIdentifiablePeerAddress o1, TemporaryIdentifiablePeerAddress o2) {
            long diff = o1.getTimeout() - o2.getTimeout();
            if(diff < 0) return -1;
            if(diff > 0) return 1;
            return 0;
        }
    }

    private class AddressStub implements TemporaryIdentifiablePeerAddress {

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
