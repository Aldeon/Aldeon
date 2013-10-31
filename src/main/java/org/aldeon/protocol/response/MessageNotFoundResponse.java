package org.aldeon.protocol.response;

import org.aldeon.protocol.Response;

public class MessageNotFoundResponse implements Response {

    public static final String TYPE = "message_not_found";

    public String type = TYPE;
}
