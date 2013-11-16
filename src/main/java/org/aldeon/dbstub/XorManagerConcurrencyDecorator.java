package org.aldeon.dbstub;

import org.aldeon.db.exception.IdentifierAlreadyPresentException;
import org.aldeon.db.exception.UnknownIdentifierException;
import org.aldeon.db.exception.UnknownParentException;
import org.aldeon.model.Identifier;

import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class XorManagerConcurrencyDecorator implements XorManager {

    private XorManager mgr;
    private ReadWriteLock rwl;

    public XorManagerConcurrencyDecorator(XorManager mgr) {
        this.mgr = mgr;
        this.rwl = new ReentrantReadWriteLock();
    }

    @Override
    public void putId(Identifier id, Identifier parent) throws UnknownParentException, IdentifierAlreadyPresentException {
        Lock l = rwl.writeLock();
        l.lock();
        try {
            mgr.putId(id, parent);
        } finally {
            l.unlock();
        }
    }

    @Override
    public void delId(Identifier id) throws UnknownIdentifierException {
        Lock l = rwl.writeLock();
        l.lock();
        try {
            mgr.delId(id);
        } finally {
            l.unlock();
        }
    }

    @Override
    public Identifier getXor(Identifier id) throws UnknownIdentifierException {
        Identifier result = null;
        Lock l = rwl.readLock();
        l.lock();
        try {
            result = mgr.getXor(id);
        } finally {
            l.unlock();
        }
        return result;
    }

    @Override
    public Set<Identifier> getIds(Identifier xor) {
        Set<Identifier> result = null;
        Lock l = rwl.readLock();
        l.lock();
        try {
            result = mgr.getIds(xor);
        } finally {
            l.unlock();
        }
        return result;
    }

    @Override
    public Set<Identifier> getChildren(Identifier parent) {
        Set<Identifier> result = null;
        Lock l = rwl.readLock();
        l.lock();
        try {
            result = mgr.getChildren(parent);
        } finally {
            l.unlock();
        }
        return result;
    }

    @Override
    public boolean contains(Identifier id) {
        boolean result;
        Lock l = rwl.readLock();
        l.lock();
        try {
            result = mgr.contains(id);
        } finally {
            l.unlock();
        }
        return result;
    }
}
