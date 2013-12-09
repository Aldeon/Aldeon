package org.aldeon.core;

import org.aldeon.db.Db;
import org.aldeon.events.EventLoop;
import org.aldeon.events.executors.ExecutorLogger;
import org.aldeon.events.executors.ThrowableInterceptor;
import org.aldeon.model.Identity;
import org.aldeon.sync.TopicManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class BaseCore implements Core {

    private final Db storage;
    private final EventLoop eventLoop;
    private final TopicManager topicManager;
    private final Set<Identity> identities = new HashSet<>();
    private final ExecutorService clientSideExecutor;
    private final ExecutorService serverSideExecutor;
    private final Executor wrappedClientExecutor;
    private final Executor wrappedServerExecutor;

    public BaseCore(Db storage, EventLoop eventLoop, TopicManager topicManager) {
        this.storage = storage;
        this.eventLoop = eventLoop;
        this.topicManager = topicManager;

        this.clientSideExecutor = Executors.newFixedThreadPool(2);
        this.serverSideExecutor = Executors.newFixedThreadPool(2);
        this.wrappedClientExecutor = new ExecutorLogger("clientSide", new ThrowableInterceptor(clientSideExecutor));
        this.wrappedServerExecutor = new ExecutorLogger("serverSide", new ThrowableInterceptor(serverSideExecutor));
    }

    @Override
    public Db getStorage() {
        return storage;
    }

    @Override
    public EventLoop getEventLoop() {
        return eventLoop;
    }

    @Override
    public Set<Identity> getAllIdentities() {
        return Collections.unmodifiableSet(identities);
    }

    @Override
    public void addIdentity(Identity identity) {
        identities.add(identity);
    }

    @Override
    public void delIdentity(Identity identity) {
        identities.remove(identity);
    }

    @Override
    public Executor serverSideExecutor() {
        return wrappedServerExecutor;
    }

    @Override
    public Executor clientSideExecutor() {
        return wrappedClientExecutor;
    }

    @Override
    public TopicManager getTopicManager() {
        return topicManager;
    }

    protected void closeExecutors() {
        clientSideExecutor.shutdown();
        serverSideExecutor.shutdown();
    }
}
