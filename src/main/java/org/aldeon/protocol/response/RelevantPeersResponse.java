package org.aldeon.protocol.response;

import org.aldeon.networking.common.PeerAddress;
import org.aldeon.protocol.Response;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class RelevantPeersResponse implements Response {

    public static final String TYPE = "relevant_peers";

    public String type = TYPE;
    public Set<PeerAddress> interested;
    public Set<PeerAddress> closest;

    public RelevantPeersResponse() {
        interested = new HashSet<>();
        closest = new HashSet<>();
    }

    public RelevantPeersResponse(Set<PeerAddress> interested,
                                 Set<PeerAddress> closest) {
        this.interested = interested;
        this.closest = closest;
    }
}
