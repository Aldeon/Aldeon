package org.aldeon.net;

import java.net.InetAddress;

public interface IpPeerAddress extends PeerAddress {
    Port getPort();
    InetAddress getHost();
}
