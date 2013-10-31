package org.aldeon.protocol.response;

import org.aldeon.model.Message;
import org.aldeon.protocol.Response;

/**
 * The message was found and is contained in the 'message' field.
 */
public class MessageFoundResponse implements Response {

    public static final String TYPE = "message_found";

    public MessageFoundResponse() {

    }

    public MessageFoundResponse(Message m) {
        this.message = m;
    }

    public String type = TYPE;
    public Message message;
}
