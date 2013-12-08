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
    public Set<PeerAddress> closestIds;

    public RelevantPeersResponse() {
        interested = new HashSet<>();
        closestIds = new HashSet<>();
    }

    public RelevantPeersResponse(Set<PeerAddress> interested,
                                 Set<PeerAddress> closestIds) {
        this.interested = interested;
        this.closestIds = closestIds;
    }
}
