package org.aldeon.net;

import org.aldeon.model.Identifier;

import java.util.Collections;
import java.util.List;

public class HiddenBehindIpv4PeerAddress implements PeerAddress {

    private final List<Ipv4PeerAddress> rendezvous;

    public HiddenBehindIpv4PeerAddress(List<Ipv4PeerAddress> rendezvous) {
        this.rendezvous = rendezvous;
    }

    public List<Ipv4PeerAddress> rendezvousPoints() {
        return Collections.unmodifiableList(rendezvous);
    }

    @Override
    public Identifier getMsgIdentifier() {
        throw new IllegalStateException("Not yet implemented");
    }
}
