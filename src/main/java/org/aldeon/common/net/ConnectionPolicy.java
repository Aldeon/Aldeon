package org.aldeon.common.net;

import java.net.InetAddress;

public interface ConnectionPolicy {
    public Port getInternalPort();
    public Port getExternalPort();
    public InetAddress getInternalAddress();
    public InetAddress getExternalAddress();
}
