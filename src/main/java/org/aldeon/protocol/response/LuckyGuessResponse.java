package org.aldeon.protocol.response;

import org.aldeon.model.Identifier;
import org.aldeon.protocol.Response;

/**
 * Server noticed that the difference between branches may be a single subbranch.
 */
public class LuckyGuessResponse implements Response {

    public static final String TYPE = "lucky_guess";

    public String type = TYPE;
    /**
     * Suggests that the person making a request is looking for this branch.
     */
    public Identifier id;

    public LuckyGuessResponse() { }
    public LuckyGuessResponse(Identifier id) {
        this.id = id;
    }
}
