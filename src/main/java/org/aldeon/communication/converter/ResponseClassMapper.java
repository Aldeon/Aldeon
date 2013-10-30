package org.aldeon.communication.converter;

import com.google.gson.JsonObject;
import org.aldeon.protocol.Response;
import org.aldeon.utils.json.ClassMapper;

/**
 * Class mapper used to decode a JSON string into a Response object.
 */
public class ResponseClassMapper implements ClassMapper<Response> {
    @Override
    public Class<? extends Response> getClass(JsonObject jsonObject) {
        return null;
    }
}
