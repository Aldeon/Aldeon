package org.aldeon.common.net.address;

/**
 * PeerAddress with a certain lifetime.
 */
public interface TemporaryIdentifiablePeerAddress extends IdentifiablePeerAddress {
    public long getTimeout();
}
