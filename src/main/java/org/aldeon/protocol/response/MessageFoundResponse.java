package org.aldeon.protocol.response;

import org.aldeon.model.Message;
import org.aldeon.protocol.Response;

public class MessageFoundResponse implements Response {

    public MessageFoundResponse() {

    }

    public MessageFoundResponse(Message m) {
        this.message = m;
    }

    public String type = "message_found";
    public Message message;
}
