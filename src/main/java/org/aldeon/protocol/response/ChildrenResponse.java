package org.aldeon.protocol.response;

import org.aldeon.model.Identifier;
import org.aldeon.protocol.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * The server responds with a set of children of a certain message.
 */
public class ChildrenResponse implements Response {

    public static final String TYPE = "children";

    public String type = TYPE;
    /**
     * A collection of pairs (id,xor). Id is unique.
     */
    public Map<Identifier, Identifier> children;

    public ChildrenResponse() {
        children = new HashMap<>();
    }
}
