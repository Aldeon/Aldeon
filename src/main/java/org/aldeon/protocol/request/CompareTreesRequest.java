package org.aldeon.protocol.request;

import org.aldeon.model.Identifier;
import org.aldeon.protocol.Request;

/**
 * Asks the peer to compare his branch with our own and find differences.
 */
public class CompareTreesRequest implements Request {

    public static String TYPE = "compare_trees";

    public String type = TYPE;
    public Identifier branchId;
    public Identifier branchXor;
    public boolean force = false;
}
