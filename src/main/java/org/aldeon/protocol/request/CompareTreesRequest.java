package org.aldeon.protocol.request;

import org.aldeon.model.Identifier;
import org.aldeon.protocol.Request;

/**
 *
 */
public class CompareTreesRequest implements Request {
    public static String TYPE = "compare_trees";
    public String type = TYPE;
    public Identifier parent_id;
    public Identifier parent_xor;
}
