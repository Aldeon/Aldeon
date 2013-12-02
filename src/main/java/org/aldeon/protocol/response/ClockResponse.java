package org.aldeon.protocol.response;

import org.aldeon.protocol.Response;

public class ClockResponse implements Response {

    public static final String TYPE = "clock";
    public String type = TYPE;

    public long clock;
}
