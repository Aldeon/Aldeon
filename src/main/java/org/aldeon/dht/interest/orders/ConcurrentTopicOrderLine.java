package org.aldeon.dht.interest.orders;

import org.aldeon.networking.common.PeerAddress;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentTopicOrderLine implements TopicOrderLine {

    private final TopicOrderLine line;
    private ReadWriteLock lock;

    public ConcurrentTopicOrderLine(TopicOrderLine line) {
        this.line = line;
        this.lock = new ReentrantReadWriteLock();
    }

    @Override
    public void addAddress(PeerAddress address) {
        lock.writeLock().lock();
        line.addAddress(address);
        lock.writeLock().unlock();
    }

    @Override
    public void delAddress(PeerAddress address) {
        lock.writeLock().lock();
        line.delAddress(address);
        lock.writeLock().unlock();
    }

    @Override
    public void addOrder(Order order) {
        lock.writeLock().lock();
        line.addOrder(order);
        lock.writeLock().unlock();
    }

    @Override
    public void delOrder(Order order) {
        lock.writeLock().lock();
        line.delOrder(order);
        lock.writeLock().unlock();
    }

    @Override
    public int getDemand() {
        lock.readLock().lock();
        int result = line.getDemand();
        lock.readLock().unlock();
        return result;
    }

    @Override
    public boolean isEmpty() {
        lock.readLock().lock();
        boolean result = line.isEmpty();
        lock.readLock().unlock();
        return result;
    }

    @Override
    public Set<PeerAddress> getAddresses(int maxResults) {
        lock.readLock().lock();
        Set<PeerAddress> result = line.getAddresses(maxResults);
        lock.readLock().unlock();
        return result;
    }
}
