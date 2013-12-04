package org.aldeon.core;


import com.google.inject.Inject;
import org.aldeon.communication.Receiver;
import org.aldeon.communication.Sender;
import org.aldeon.communication.task.InboundRequestTask;
import org.aldeon.core.events.AppClosingEvent;
import org.aldeon.core.events.InboundRequestEvent;
import org.aldeon.db.Db;
import org.aldeon.dht.Dht;
import org.aldeon.dht.DhtModule;
import org.aldeon.events.ACB;
import org.aldeon.events.AsyncCallback;
import org.aldeon.events.EventLoop;
import org.aldeon.model.Identity;
import org.aldeon.net.PeerAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
    private final Db storage;

    private final ExecutorService clientSideExecutor;
    private final ExecutorService serverSideExecutor;
    private final Executor wrappedClientExecutor;
    private final Executor wrappedServerExecutor;

    private final Map<Class, Sender> senders;
    private final Map<Class, Receiver> receivers;
    private final Set<Identity> identies;
    private final Map<Class, Dht> dhts;


    @Inject
    public CoreImpl(Db storage, EventLoop eventLoop) {
        this.storage = storage;
        this.eventLoop = eventLoop;

        this.identies = new HashSet<>();
        this.clientSideExecutor = Executors.newFixedThreadPool(2);
        this.serverSideExecutor = Executors.newFixedThreadPool(2);
        this.wrappedClientExecutor = new ThrowableInterceptor(clientSideExecutor);
        this.wrappedServerExecutor = new ThrowableInterceptor(serverSideExecutor);


        senders = new HashMap<>();
        receivers = new HashMap<>();
        dhts = new HashMap<>();


        log.debug("Initialized the core.");

        getEventLoop().assign(AppClosingEvent.class, new ACB<AppClosingEvent>(clientSideExecutor()) {
            @Override
            public void react(AppClosingEvent val) {
                close();
            }
        });
    }

    @Override
    public <T extends PeerAddress> Dht<T> getDht(Class<T> addressType) {
        return dhts.get(addressType);
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
        return Collections.unmodifiableSet(identies);
    }

    @Override
    public void addIdentity(Identity identity) {
        identies.add(identity);
    }

    @Override
    public void delIdentity(Identity identity) {
        identies.remove(identity);
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
    public <T extends PeerAddress> void registerSender(Class<T> addressType, Sender<T> sender) {
        senders.put(addressType, sender);

        // Dht is registered as soon as a sender is available
        dhts.put(addressType, DhtModule.createDht(sender, clientSideExecutor(), addressType));
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
    public void initReceivers() {
        AsyncCallback<InboundRequestTask> callback = new ACB<InboundRequestTask>(serverSideExecutor()) {

            /*
                Notify all listeners that the new request arrived
             */

            @Override
            protected void react(InboundRequestTask val) {
                getEventLoop().notify(new InboundRequestEvent(val));
            }
        };

        for(Receiver receiver: receivers.values()) {
            receiver.setCallback(callback);
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
