package org.aldeon.dht.closeness;

import org.aldeon.model.Identifier;
import org.aldeon.networking.common.PeerAddress;

import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class ConcurrentRingDecorator implements Ring {

    private final Ring ring;
    private final ReadWriteLock lock;

    public ConcurrentRingDecorator(Ring ring) {
        this.ring = ring;
        this.lock = new ReentrantReadWriteLock();
    }

    @Override
    public void insert(PeerAddress address) {
        lock.writeLock().lock();
        ring.insert(address);
        lock.writeLock().unlock();
    }

    @Override
    public void remove(PeerAddress address) {
        lock.writeLock().lock();
        ring.remove(address);
        lock.writeLock().unlock();
    }

    @Override
    public Set<PeerAddress> getNearest(Identifier identifier, int maxResults) {
        lock.readLock().lock();
        Set<PeerAddress> result = ring.getNearest(identifier, maxResults);
        lock.readLock().unlock();
        return result;
    }
}
