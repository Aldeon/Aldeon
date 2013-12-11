package org.aldeon.core;

import org.aldeon.db.Db;
import org.aldeon.db.wrappers.DbLoggerDecorator;
import org.aldeon.events.EventLoop;
import org.aldeon.events.executors.ExecutorLogger;
import org.aldeon.events.executors.ThrowableInterceptor;
import org.aldeon.model.Identity;
import org.aldeon.sync.Supervisor;
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
    private final ExecutorService clientSideExecutor;
    private final ExecutorService serverSideExecutor;
    private final Executor wrappedClientExecutor;
    private final Executor wrappedServerExecutor;
    private final Thread supervisorThread;
    private final UserManager userManager;

    public BaseCore(Db storage, EventLoop eventLoop, TopicManager topicManager) {

        //this.storage = storage;
        this.userManager = new UserManager();
        this.storage = new DbLoggerDecorator(storage);
        this.eventLoop = eventLoop;
        this.topicManager = topicManager;

        storage.start();

        this.clientSideExecutor = Executors.newFixedThreadPool(2);
        this.serverSideExecutor = Executors.newFixedThreadPool(2);
        this.wrappedClientExecutor = new ExecutorLogger("clientSide", new ThrowableInterceptor(clientSideExecutor));
        this.wrappedServerExecutor = new ExecutorLogger("serverSide", new ThrowableInterceptor(serverSideExecutor));

        supervisorThread = new Thread() {
            @Override
            public void run() {
                Runnable supervisor = new Supervisor(getTopicManager(), clientSideExecutor());

                while(true) {
                    supervisor.run();
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        };
        supervisorThread.start();
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

    @Override
    public UserManager getUserManager(){
        return userManager;
    }

    protected void closeExecutors() {
        clientSideExecutor.shutdown();
        serverSideExecutor.shutdown();
        supervisorThread.interrupt();
    }

    protected void closeDb() {
        storage.close();
    }
}
