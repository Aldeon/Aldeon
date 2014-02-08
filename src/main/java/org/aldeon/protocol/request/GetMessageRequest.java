package org.aldeon.protocol.request;

import org.aldeon.model.Identifier;
import org.aldeon.protocol.Request;

/**
 * Asks the server if the message with a certain ID is available.
 */
public class GetMessageRequest implements Request {

    public static String TYPE = "get_message";

    public String type = TYPE;
    public Identifier id;
}
