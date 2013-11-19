package org.aldeon.protocol.request;

import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Request;

import java.util.HashSet;
import java.util.Set;


public class GetRelevantPeersRequest implements Request {

    public static String TYPE = "get_relevant_peers";

    public String type = TYPE;
    public Identifier target;
    public Set<Class<? extends PeerAddress>> acceptedTypes;

    public GetRelevantPeersRequest() {
        acceptedTypes = new HashSet<>();
    }
}
