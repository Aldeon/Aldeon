package org.aldeon.communication.netty;

import com.google.gson.JsonObject;
import org.aldeon.protocol.Response;
import org.aldeon.utils.json.ClassMapper;

public class ResponseClassMapper implements ClassMapper<Response> {
    @Override
    public Class<? extends Response> getClass(JsonObject jsonObject) {
        return null;
    }
}
