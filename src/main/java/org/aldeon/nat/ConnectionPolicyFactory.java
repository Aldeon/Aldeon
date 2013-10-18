package org.aldeon.nat;

import org.aldeon.common.net.ConnectionPolicy;

public interface ConnectionPolicyFactory {
    public void begin();
    public void abort();
    public boolean isReady();
    public boolean didFail();
    public ConnectionPolicy getPolicy();
}
