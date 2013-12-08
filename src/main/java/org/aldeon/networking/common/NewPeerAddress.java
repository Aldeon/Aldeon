package org.aldeon.networking.common;

import org.aldeon.model.Identifiable;

public interface NewPeerAddress extends Identifiable {
    NewPeerAddressType getType();
}
