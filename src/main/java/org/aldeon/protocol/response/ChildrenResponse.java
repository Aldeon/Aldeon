package org.aldeon.protocol.response;

import org.aldeon.model.Identifier;
import org.aldeon.protocol.Response;

import java.util.ArrayList;

/**
 *
 */
public class ChildrenResponse implements Response {
    public static final String TYPE = "children";
    public String type = TYPE;

    public ArrayList<Identifier> children;

    public ChildrenResponse() {
        children = new ArrayList<>();
    }
    public ChildrenResponse(ArrayList<Identifier> children) {
        this.children = children;
    }
}
