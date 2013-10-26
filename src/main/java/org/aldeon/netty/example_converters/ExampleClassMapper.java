package org.aldeon.netty.example_converters;

import com.google.gson.JsonObject;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.example.ExampleDateRequest;
import org.aldeon.utils.json.ClassMapper;

public class ExampleClassMapper implements ClassMapper<Request> {
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
