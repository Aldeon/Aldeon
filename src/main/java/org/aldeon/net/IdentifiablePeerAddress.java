package org.aldeon.net;

import org.aldeon.model.Identifiable;

/**
 * Represents a PeerAddress with associated Identifier.
 * These addresses can be stored in a DHT.
 */
public interface IdentifiablePeerAddress extends Identifiable, PeerAddress{
}
