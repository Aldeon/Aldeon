package org.aldeon.communication.converter;

import com.google.gson.JsonObject;
import org.aldeon.common.protocol.Request;
import org.aldeon.protocol.example.ExampleDateRequest;
import org.aldeon.utils.json.ClassMapper;

public class RequestClassMapper implements ClassMapper<Request> {
    @Override
    public Class<? extends Request> getClass(JsonObject jsonObject) {
        String action = jsonObject.get("action").getAsString();
        if(action.equals("date")) {
            return ExampleDateRequest.class;
        } else {
            return null;
        }
    }
}
