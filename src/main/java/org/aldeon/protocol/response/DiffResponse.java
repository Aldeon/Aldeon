package org.aldeon.protocol.response;

import org.aldeon.model.Identifier;
import org.aldeon.protocol.Response;

import java.util.Map;

public class DiffResponse implements Response {

    public static final String TYPE = "diff";
    public String type = TYPE;

    public long clock;

    /**
     * Collection of id-parent pairs.
     */
    public Map<Identifier, Identifier> ids;
}
