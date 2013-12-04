package org.aldeon.protocol.request;

import org.aldeon.model.Identifier;
import org.aldeon.protocol.Request;

public class GetDiffRequest implements Request {

    public static String TYPE = "get_diff";

    public String type = TYPE;
    public Identifier topic;
    public long clock;
}
