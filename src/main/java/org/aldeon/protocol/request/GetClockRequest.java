package org.aldeon.protocol.request;

import org.aldeon.model.Identifier;
import org.aldeon.protocol.Request;

public class GetClockRequest implements Request {

    public static String TYPE = "get_clock";

    public String type = TYPE;
    public Identifier topic;
}
