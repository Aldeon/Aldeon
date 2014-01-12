package org.aldeon.networking.mediums.ip.discovery;

import org.aldeon.core.services.Service;
import org.aldeon.core.services.ServiceManager;
import org.aldeon.events.Callback;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;
import org.aldeon.networking.mediums.ip.addresses.IpV4PeerAddress;
import org.aldeon.utils.helpers.InetAddresses;
import org.aldeon.utils.various.LoopWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

public class LocalPeerDiscoveryService implements Service, Callback<IpPeerAddress> {

    private static final Logger log = LoggerFactory.getLogger(LocalPeerDiscoveryService.class);

    private static final int DISCOVERY_PORT = 8999;
    private static final int INTERVAL = 10000;
    private final ServiceManager services;
    private final Set<IpPeerAddress> advertisedAddresses = new HashSet<>();
    private final Callback<IpPeerAddress> onFound;

    public LocalPeerDiscoveryService(Callback<IpPeerAddress> onFound) {
        services = new ServiceManager();
        services.registerService(new UdpDiscoveryServer(DISCOVERY_PORT, this));
        this.onFound = onFound;
    }


    public void advertiseAddress(IpPeerAddress address, int mask) {
        if(address.getType().equals(IpPeerAddress.IPV4)) {
            log.info("Advertising " + address + " on local network /" + mask);
            advertisedAddresses.add(address);
            IpV4PeerAddress addr = (IpV4PeerAddress) address;
            InetAddress bcast = InetAddresses.makeBroadcastAddress(addr.getHost(), mask);
            services.registerService(new LoopWorker(INTERVAL, new UdpDiscoveryClient(addr, bcast, DISCOVERY_PORT)));
        }
    }

    @Override
    public void start() {
        services.start();
    }

    @Override
    public void close() {
        services.close();
    }

    @Override
    public void call(IpPeerAddress address) {
        // Called when a peer address is received through discovery port
        if(!advertisedAddresses.contains(address)) {
            onFound.call(address);
        }
    }
}
