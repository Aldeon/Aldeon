package org.aldeon.protocol.request;

import org.aldeon.model.Identifier;
import org.aldeon.protocol.Request;


public class GetRelevantPeersRequest implements Request {

    public static String TYPE = "get_relevant_peers";

    public String type = TYPE;
    public Identifier target;

}
