package org.aldeon.core;


import com.google.inject.Inject;
import org.aldeon.events.EventLoop;
import org.aldeon.db.Storage;
import org.aldeon.net.PeerAddress;
import org.aldeon.communication.Receiver;
import org.aldeon.communication.Sender;
import org.aldeon.communication.task.InboundRequestTask;
import org.aldeon.core.events.AppClosingEvent;
import org.aldeon.core.events.TopicAddedEvent;
import org.aldeon.protocol.Protocol;
import org.aldeon.events.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Application core. Aggregates all the services necessary for
 * proper functioning of server-side and client-side elements.
 *
 */
public class CoreImpl implements Core {

    private static final Logger log = LoggerFactory.getLogger(CoreImpl.class);

    private final EventLoop eventLoop;
    private final Storage storage;
    private final ExecutorService clientSideExecutor;
    private final ExecutorService serverSideExecutor;

    private final Map<Class, Sender> senders;
    private final Map<Class, Receiver> receivers;

    @Inject
    public CoreImpl(Storage storage, EventLoop eventLoop) {
        this.storage = storage;
        this.eventLoop = eventLoop;
        this.clientSideExecutor = Executors.newFixedThreadPool(2);
        this.serverSideExecutor = Executors.newFixedThreadPool(2);

        senders = new HashMap<>();
        receivers = new HashMap<>();

        log.debug("Initialized the core.");

        registerEventCallbacks();
    }

    private void registerEventCallbacks() {
        getEventLoop().assign(TopicAddedEvent.class, new TopicManagerCallback(this), clientSideExecutor());
        getEventLoop().assign(AppClosingEvent.class, new Callback<AppClosingEvent>() {
            @Override
            public void call(AppClosingEvent val) {
                close();
            }
        }, clientSideExecutor());
    }

    @Override
    public Storage getStorage() {
        return storage;
    }

    @Override
    public EventLoop getEventLoop() {
        return eventLoop;
    }

    @Override
    public Executor serverSideExecutor() {
        return serverSideExecutor;
    }

    @Override
    public Executor clientSideExecutor() {
        return clientSideExecutor;
    }

    @Override
    public <T extends PeerAddress> void registerSender(Class<T> addressType, Sender<T> sender) {
        senders.put(addressType, sender);
    }

    @Override
    public <T extends PeerAddress> void registerReceiver(Class<T> addressType, Receiver<T> receiver) {
        receivers.put(addressType, receiver);
    }

    @Override
    public <T extends PeerAddress> Sender<T> getSender(Class<T> addressType) {
        return senders.get(addressType);
    }

    @Override
    public <T extends PeerAddress> Receiver<T> getReceiver(Class<T> addressType) {
        return receivers.get(addressType);
    }

    @Override
    public void initSenders() {
        for(Sender sender: senders.values()) {
            sender.start();
        }
    }

    @Override
    public void initReceivers(Protocol protocol) {
        Callback<InboundRequestTask> callback = new ResponderCallback(protocol, serverSideExecutor());

        for(Receiver receiver: receivers.values()) {
            receiver.setCallback(callback, serverSideExecutor());
            receiver.start();
        }
    }

    private void close() {
        log.debug("Closing the core...");
        for(Sender sender: senders.values()) {
            sender.close();
        }

        for(Receiver receiver: receivers.values()) {
            receiver.close();
        }

        clientSideExecutor.shutdown();
        serverSideExecutor.shutdown();
        log.debug("Core closed successfully.");
    }

}
