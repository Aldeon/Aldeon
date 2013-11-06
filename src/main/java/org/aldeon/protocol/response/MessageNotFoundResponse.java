package org.aldeon.protocol.response;

import org.aldeon.protocol.Response;

/**
 * The message in question was not found on the server.
 */
public class MessageNotFoundResponse implements Response {

    public static final String TYPE = "message_not_found";

    public String type = TYPE;
}
