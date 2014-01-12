package org.aldeon.dht;

import com.google.common.net.HostAndPort;
import org.aldeon.config.Config;
import org.aldeon.dht.closeness.ClosenessTracker;
import org.aldeon.dht.closeness.ClosenessTrackerDispatcher;
import org.aldeon.dht.closeness.ClosenessTrackerFilter;
import org.aldeon.dht.closeness.RingBasedClosenessTracker;
import org.aldeon.dht.closeness.RingImpl;
import org.aldeon.dht.interest.AddressTypeIgnoringInterestTracker;
import org.aldeon.dht.interest.InterestTracker;
import org.aldeon.dht.interest.InterestTrackerDispatcher;
import org.aldeon.dht.interest.InterestTrackerEventCaller;
import org.aldeon.dht.interest.InterestTrackerFilter;
import org.aldeon.events.EventLoop;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;
import org.aldeon.utils.various.Predicate;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DhtModule {

    private static final Logger log = LoggerFactory.getLogger(DhtModule.class);

    public static Dht create(Set<AddressType> acceptedTypes, EventLoop eventLoop, Predicate<PeerAddress> filter) {

        // 1. Allocate appropriate structures

        Map<AddressType, ClosenessTracker> closenessTrackerMap = new HashMap<>();
        Map<AddressType, InterestTracker> interestTrackerMap = new HashMap<>();

        for(AddressType type: acceptedTypes) {
            closenessTrackerMap.put(type, new RingBasedClosenessTracker(new RingImpl()));
            interestTrackerMap.put(type, new AddressTypeIgnoringInterestTracker());
        }

        // 2. Create AddressType-based dispatchers

        ClosenessTracker closenessTracker = new ClosenessTrackerDispatcher(closenessTrackerMap);
        InterestTracker interestTracker = new InterestTrackerDispatcher(interestTrackerMap);

        // 3. Apply decorators

        closenessTracker = new ClosenessTrackerFilter(closenessTracker, filter);

        interestTracker = new InterestTrackerEventCaller(interestTracker, eventLoop);
        interestTracker = new InterestTrackerFilter(interestTracker, filter);

        // 4. Insert initial peers from config
        for(String peer: Config.config().getStringArray("peers.initial")) {
            try {
                HostAndPort hnp = HostAndPort.fromString(peer).withDefaultPort(80);
                InetAddress host = InetAddress.getByName(hnp.getHostText());
                int port = hnp.getPort();
                PeerAddress peerAddress = IpPeerAddress.create(host, port);
                closenessTracker.addAddress(peerAddress);
                log.info("Added initial peer: " + peerAddress);
            } catch (Exception e) {
                System.out.println("Failed to parse address " + peer);
            }
        }

        // 5. Return dht object

        final InterestTracker it = interestTracker;
        final ClosenessTracker ct = closenessTracker;

        return new Dht() {
            @Override
            public InterestTracker interestTracker() {
                return it;
            }

            @Override
            public ClosenessTracker closenessTracker() {
                return ct;
            }
        };
    }
}
