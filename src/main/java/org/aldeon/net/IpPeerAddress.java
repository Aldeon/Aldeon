package org.aldeon.net;

import java.net.InetAddress;

public abstract class IpPeerAddress implements PeerAddress {
    public abstract Port getPort();
    public abstract InetAddress getHost();

    @Override
    public int hashCode() {
        return getIdentifier().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof IpPeerAddress) {
            IpPeerAddress that = (IpPeerAddress) obj;
            return this.getPort().equals(that.getPort()) && this.getHost().equals(that.getHost());
        } else {
            return false;
        }
    }
}
