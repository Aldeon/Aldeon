package org.aldeon.protocol.response;

import org.aldeon.model.Identifier;
import org.aldeon.protocol.Response;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The server responds with a set of children of a certain message.
 */
public class ChildrenResponse implements Response {

    public static final String TYPE = "children";

    public String type = TYPE;
    /**
     * A collection of pairs (id,xor). Id is unique.
     */
    public Set<IdAndXor> children;

    public ChildrenResponse() {
        children = new HashSet<>();
    }

    public static class IdAndXor{
        public Identifier id;
        public Identifier xor;

        public IdAndXor(Identifier id, Identifier xor) {
            this.id = id;
            this.xor = xor;
        }

        public IdAndXor() {}
    }
}
