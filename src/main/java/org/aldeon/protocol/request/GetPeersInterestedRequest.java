package org.aldeon.protocol.request;

import org.aldeon.model.Identifier;
import org.aldeon.protocol.Request;

/**
 *
 */
public class GetPeersInterestedRequest implements Request {
    public static String TYPE = "get_peers_interested";
    public String type = TYPE;

    /**
     * Points to a topic the requester is interested in.
     */
    public Identifier target;
}
