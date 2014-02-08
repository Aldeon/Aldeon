package org.aldeon.protocol.response;

import org.aldeon.protocol.Response;

/**
 * There are no xor differences between server and client.
 */
public class BranchInSyncResponse implements Response {

    public static final String TYPE = "branch_in_sync";

    public String type = TYPE;
}
