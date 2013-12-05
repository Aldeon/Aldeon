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
import org.aldeon.net.AddressType;
import org.aldeon.sync.TopicManager;
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

    private final Map<AddressType, Sender> senders = new HashMap<>();
    private final Map<AddressType, Dht> dhts = new HashMap<>();
    private final Set<Receiver> receivers = new HashSet<>();

    private final Set<Identity> identities = new HashSet<>();

    private final TopicManager topicManager;


    @Inject
    public CoreImpl(Db storage, EventLoop eventLoop, Set<Sender> sendersList, Set<Receiver> receiversList) {

        this.storage = storage;
        this.eventLoop = eventLoop;

        this.topicManager = new TopicManager();
        this.clientSideExecutor = Executors.newFixedThreadPool(2);
        this.serverSideExecutor = Executors.newFixedThreadPool(2);
        this.wrappedClientExecutor = new ThrowableInterceptor(clientSideExecutor);
        this.wrappedServerExecutor = new ThrowableInterceptor(serverSideExecutor);

        // Initialize all senders and receivers

        getEventLoop().assign(AppClosingEvent.class, new ACB<AppClosingEvent>(clientSideExecutor()) {
            @Override
            public void react(AppClosingEvent val) {
                close();
            }
        });

        AsyncCallback<InboundRequestTask> callback = new ACB<InboundRequestTask>(serverSideExecutor()) {
            @Override
            protected void react(InboundRequestTask val) {
                getEventLoop().notify(new InboundRequestEvent(val));
            }
        };

        for(Sender sender: sendersList) {
            registerSender(sender);
            sender.start();
        }

        for(Receiver receiver: receiversList) {
            registerReceiver(receiver);
            receiver.setCallback(callback);
            receiver.start();
        }

        log.debug("Initialized the core.");
    }

    @Override
    public Dht getDht(AddressType addressType) {
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
    public Sender getSender(AddressType addressType) {
        return senders.get(addressType);
    }

    @Override
    public TopicManager getTopicManager() {
        return topicManager;
    }

    // /////////////////////////////////////////////////////////////////////////////
    // /////////////////////////////////////////////////////////////////////////////
    // /////////////////////////////////////////////////////////////////////////////

    private void registerSender(Sender sender) {
        senders.put(sender.getAcceptedType(), sender);
        dhts.put(sender.getAcceptedType(), DhtModule.createDht(sender));
    }

    private void registerReceiver(Receiver receiver) {
        receivers.add(receiver);
    }

    private void close() {
        log.debug("Closing the core...");
        for(Sender sender: senders.values()) {
            sender.close();
        }

        for(Receiver receiver: receivers) {
            receiver.close();
        }

        clientSideExecutor.shutdown();
        serverSideExecutor.shutdown();
        log.debug("Core closed successfully.");
    }

}
