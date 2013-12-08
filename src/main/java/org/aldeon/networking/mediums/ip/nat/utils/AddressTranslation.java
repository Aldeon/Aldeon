package org.aldeon.networking.mediums.ip.nat.utils;

import org.aldeon.networking.common.Port;

import java.net.InetAddress;

/**
 * Represents how a service is configured locally and how it is visible for others.
 */
public interface AddressTranslation {
    public Port getInternalPort();
    public Port getExternalPort();
    public InetAddress getInternalAddress();
    public InetAddress getExternalAddress();
    public void shutdown();
}
