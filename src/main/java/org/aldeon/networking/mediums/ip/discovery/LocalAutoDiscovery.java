package org.aldeon.networking.mediums.ip.discovery;

import org.aldeon.core.services.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetAddress;

public class LocalAutoDiscovery implements Service {

    private static final Logger log = LoggerFactory.getLogger(LocalAutoDiscovery.class);
    private final int subnetMask;
    private final InetAddress localAddress;

    public LocalAutoDiscovery(InetAddress localAddress, int subnetMask) {
        this.localAddress = localAddress;
        this.subnetMask = subnetMask;
    }

    @Override
    public void start() {
        log.info("Starting local peer discovery for address " + localAddress.getHostAddress() + " on subnet /" + subnetMask + "...");
    }

    @Override
    public void close() {
        log.info("Shutting down local peer discovery for address " + localAddress.getHostAddress() + "...");

    }
}
