package org.aldeon.core;


import com.google.inject.Inject;
import org.aldeon.communication.Receiver;
import org.aldeon.communication.Sender;
import org.aldeon.communication.task.InboundRequestTask;
import org.aldeon.core.events.AppClosingEvent;
import org.aldeon.core.events.InboundRequestEvent;
import org.aldeon.core.senderforwarding.SenderDispatcher;
import org.aldeon.db.Db;
import org.aldeon.dht.Dht;
import org.aldeon.dht.DhtModule;
import org.aldeon.events.ACB;
import org.aldeon.events.AsyncCallback;
import org.aldeon.events.EventLoop;
import org.aldeon.net.AddressType;
import org.aldeon.sync.TopicManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Application core. Aggregates all the services necessary for
 * proper functioning of server-side and client-side elements.
 *
 */
public class CoreWithPredefinedEndpoints extends BaseCore {

    private static final Logger log = LoggerFactory.getLogger(CoreWithPredefinedEndpoints.class);

    private final Set<Sender> senders;
    private final Map<AddressType, Dht> dhts = new HashMap<>();
    private final Set<Receiver> receivers = new HashSet<>();
    private final Sender senderDispatcher;

    @Inject
    public CoreWithPredefinedEndpoints(Db storage, EventLoop eventLoop, TopicManager topicManager, Set<Sender> sendersList, Set<Receiver> receiversList) {
        super(storage, eventLoop, topicManager);

        // Initialize all senders and receivers

        getEventLoop().assign(AppClosingEvent.class, new ACB<AppClosingEvent>(clientSideExecutor()) {
            @Override
            public void react(AppClosingEvent val) {
                closeServices();
                closeExecutors();
                log.debug("Core closed successfully.");
            }
        });

        AsyncCallback<InboundRequestTask> callback = new ACB<InboundRequestTask>(serverSideExecutor()) {
            @Override
            protected void react(InboundRequestTask val) {
                getEventLoop().notify(new InboundRequestEvent(val));
            }
        };

        this.senders = sendersList;
        this.senderDispatcher = new SenderDispatcher(senders);

        for(Sender sender: sendersList) {
            sender.start();
            for(AddressType addressType: sender.acceptedTypes()) {
                initDht(addressType);
            }
        }

        for(Receiver receiver: receiversList) {
            receivers.add(receiver);
            receiver.setCallback(callback);
            receiver.start();
        }

        log.debug("Initialized the core.");
    }

    @Override
    public Dht getDht(AddressType addressType) {
        return dhts.get(addressType);
    }

    private void initDht(AddressType addressType) {
        if(! dhts.containsKey(addressType)) {
            dhts.put(addressType, DhtModule.createDht(getSender(), addressType));
        }
    }

    @Override
    public Sender getSender() {
        return senderDispatcher;
    }

    // /////////////////////////////////////////////////////////////////////////////
    // /////////////////////////////////////////////////////////////////////////////
    // /////////////////////////////////////////////////////////////////////////////

    private void closeServices() {

        log.debug("Closing the core...");

        for(Sender sender: senders) {
            sender.close();
        }

        for(Receiver receiver: receivers) {
            receiver.close();
        }
    }
}
