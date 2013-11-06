package org.aldeon.protocol.response;

import org.aldeon.model.Identifier;
import org.aldeon.protocol.Response;

import java.util.HashSet;
import java.util.Set;

/**
 * The server responds with a set of children of a certain message.
 */
public class ChildrenResponse implements Response {

    public static final String TYPE = "children";

    public String type = TYPE;
    public Set<Identifier> children;

    public ChildrenResponse() {
        children = new HashSet<>();
    }
    public ChildrenResponse(Set<Identifier> children) {
        this.children = children;
    }
}
