package org.aldeon.core;


import com.google.inject.Inject;
import org.aldeon.core.events.AppClosingEvent;
import org.aldeon.core.events.InboundRequestEvent;
import org.aldeon.core.services.Service;
import org.aldeon.db.Db;
import org.aldeon.dht.Dht;
import org.aldeon.dht.DhtModule;
import org.aldeon.dht.crawler.Crawler;
import org.aldeon.dht.interest.DemandChangedEvent;
import org.aldeon.events.ACB;
import org.aldeon.events.EventLoop;
import org.aldeon.networking.AddressFilter;
import org.aldeon.networking.NetworkService;
import org.aldeon.networking.common.InboundRequestTask;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.Receiver;
import org.aldeon.networking.common.Sender;
import org.aldeon.sync.Supervisor;
import org.aldeon.sync.TopicManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Application core. Aggregates all the services necessary for
 * proper functioning of server-side and client-side elements.
 *
 */
public class AldeonCore extends BaseCore {

    private static final Logger log = LoggerFactory.getLogger(AldeonCore.class);

    private final Dht dht;
    private final Crawler crawler;
    private final Sender sender;
    private final Receiver receiver;
    private final NetworkService networkService;

    @Inject
    public AldeonCore(Db storage, EventLoop eventLoop, TopicManager topicManager, NetworkService networkService) {
        super(storage, eventLoop, topicManager);

        this.networkService = networkService;
        this.sender = networkService.getUnifiedSender();
        this.receiver = networkService.getUnifiedReceiver();
        this.dht = DhtModule.create(getSender().acceptedTypes(), eventLoop, new AddressFilter(networkService));
        this.crawler = new Crawler(getDht(), getSender());

        getEventLoop().assign(DemandChangedEvent.class, new ACB<DemandChangedEvent>(clientSideExecutor()) {
            @Override
            protected void react(DemandChangedEvent val) {
                crawler.handleDemand(val.topic(), val.addressType());
            }
        });

        getEventLoop().assign(AppClosingEvent.class, new ACB<AppClosingEvent>(clientSideExecutor()) {
            @Override
            public void react(AppClosingEvent val) {
                close();
                log.debug("Core closed successfully.");
            }
        });

        getReceiver().setCallback(new ACB<InboundRequestTask>(serverSideExecutor()) {
            @Override
            protected void react(InboundRequestTask val) {
                getEventLoop().notify(new InboundRequestEvent(val));
            }
        });

        registerService(networkService);
        registerService(getSender());
        registerService(getReceiver());
        registerService(crawler);
        registerService(new Supervisor(this, topicManager, clientSideExecutor()));

        start();
        log.debug("Initialized the core.");
    }

    @Override
    public Dht getDht() {
        return dht;
    }

    @Override
    public PeerAddress reachableLocalAddress(PeerAddress peer) {
        return networkService.localAddress(peer);
    }

    @Override
    public Sender getSender() {
        return sender;
    }

    @Override
    public Receiver getReceiver() {
        return receiver;
    }
}
