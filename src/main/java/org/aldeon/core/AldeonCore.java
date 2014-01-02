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
import org.aldeon.events.AsyncCallback;
import org.aldeon.events.EventLoop;
import org.aldeon.networking.NetworkState;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.InboundRequestTask;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.Receiver;
import org.aldeon.networking.common.Sender;
import org.aldeon.sync.Supervisor;
import org.aldeon.sync.TopicManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.print.resources.serviceui_sv;


/**
 * Application core. Aggregates all the services necessary for
 * proper functioning of server-side and client-side elements.
 *
 */
public class AldeonCore extends BaseCore implements Service {

    private static final Logger log = LoggerFactory.getLogger(AldeonCore.class);

    private final Dht dht;
    private final Crawler crawler;
    private final Sender sender;
    private final Receiver receiver;

    @Inject
    public AldeonCore(Db storage, EventLoop eventLoop, TopicManager topicManager, NetworkState networkState) {
        super(storage, eventLoop, topicManager);

        this.sender = networkState.getUnifiedSender();
        this.receiver = networkState.getUnifiedReceiver();
        this.dht = DhtModule.create(getSender().acceptedTypes(), eventLoop);
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

        for(AddressType type: getSender().acceptedTypes()) {
            for(PeerAddress machineAddress: networkState.getMachineAddresses(type)) {
                if(machineAddress != null) {
                    log.info("Detected address (" + machineAddress.getType() + "): " + machineAddress);
                }
            }
        }

        for(PeerAddress address: getPropertiesManager().getInitPeers()) {
            getDht().closenessTracker().addAddress(address);
            log.info("Adding initial peer: " + address);
        }

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
    public Sender getSender() {
        return sender;
    }

    @Override
    public Receiver getReceiver() {
        return receiver;
    }
}
