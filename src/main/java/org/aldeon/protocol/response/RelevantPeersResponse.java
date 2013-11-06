package org.aldeon.protocol.response;

import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Response;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class RelevantPeersResponse implements Response {

    public static final String TYPE = "relevant_peers";

    public String type = TYPE;
    public HashSet<PeerAddress> interested;
    public HashSet<PeerAddress> closestIds;

    public RelevantPeersResponse() {
        interested = new HashSet<PeerAddress>();
        closestIds = new HashSet<PeerAddress>();
    }

    public RelevantPeersResponse(HashSet<PeerAddress> interested,
                                 HashSet<PeerAddress> closestIds) {
        this.interested = interested;
        this.closestIds = closestIds;
    }
}
