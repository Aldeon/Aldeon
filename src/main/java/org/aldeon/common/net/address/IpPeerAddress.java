package org.aldeon.common.net.address;

import org.aldeon.common.net.Port;

import java.net.InetAddress;

public interface IpPeerAddress extends PeerAddress {
    Port getPort();
    InetAddress getHost();
}
