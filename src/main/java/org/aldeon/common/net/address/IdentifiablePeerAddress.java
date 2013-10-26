package org.aldeon.common.net.address;

import org.aldeon.common.model.Identifiable;

/**
 * Represents a PeerAddress with associated Identifier.
 * These addresses can be stored in a DHT.
 */
public interface IdentifiablePeerAddress extends Identifiable, PeerAddress{
}
