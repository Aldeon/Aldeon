package org.aldeon.core;

import org.aldeon.core.services.ExecutorPoolService;
import org.aldeon.core.services.ServiceManager;
import org.aldeon.db.Db;
import org.aldeon.db.wrappers.DbEventCallerDecorator;
import org.aldeon.events.EventLoop;
import org.aldeon.events.executors.ExecutorLogger;
import org.aldeon.events.executors.ThrowableInterceptor;
import org.aldeon.sync.TopicManager;

import java.util.concurrent.Executor;

public abstract class BaseCore extends ServiceManager implements Core {

    private final Db storage;
    private final EventLoop eventLoop;
    private final TopicManager topicManager;
    private final Executor clientExecutor;
    private final Executor serverExecutor;
    private final UserManager userManager = new UserManager();
    private final PropertiesManager propertiesManager = new PropertiesManager();

    public BaseCore(Db storage, EventLoop eventLoop, TopicManager topicManager) {

        this.storage = new DbEventCallerDecorator(storage, eventLoop);
        this.eventLoop = eventLoop;
        this.topicManager = topicManager;

        ExecutorPoolService clientSideExecutor = new ExecutorPoolService(2);
        ExecutorPoolService serverSideExecutor = new ExecutorPoolService(2);
        this.clientExecutor = new ExecutorLogger("client", new ThrowableInterceptor(clientSideExecutor));
        this.serverExecutor = new ExecutorLogger("server", new ThrowableInterceptor(serverSideExecutor));

        registerService(clientSideExecutor);
        registerService(serverSideExecutor);
        registerService(storage);
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
    public Executor serverSideExecutor() {
        return serverExecutor;
    }

    @Override
    public Executor clientSideExecutor() {
        return clientExecutor;
    }

    @Override
    public TopicManager getTopicManager() {
        return topicManager;
    }

    @Override
    public UserManager getUserManager(){
        return userManager;
    }

    @Override
    public PropertiesManager getPropertiesManager() { return propertiesManager; }
}
