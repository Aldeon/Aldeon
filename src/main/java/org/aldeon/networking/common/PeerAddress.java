package org.aldeon.networking.common;

import org.aldeon.model.Identifiable;

public interface PeerAddress extends Identifiable {
    AddressType getType();
}
