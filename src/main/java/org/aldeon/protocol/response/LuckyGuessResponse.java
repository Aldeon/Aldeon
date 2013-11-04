package org.aldeon.protocol.response;

import org.aldeon.model.Identifier;
import org.aldeon.protocol.Response;

/**
 *
 */
public class LuckyGuessResponse implements Response {
    public static final String TYPE = "lucky_guess";
    public String type = TYPE;

    public Identifier id;

    public LuckyGuessResponse() { }
    public LuckyGuessResponse(Identifier id) {
        this.id = id;
    }
}
