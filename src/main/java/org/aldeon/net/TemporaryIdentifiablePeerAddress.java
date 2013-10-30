package org.aldeon.net;

/**
 * PeerAddress with a certain lifetime.
 */
public interface TemporaryIdentifiablePeerAddress extends IdentifiablePeerAddress {
    public long getTimeout();
}
