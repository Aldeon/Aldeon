package org.aldeon.jetty.json;

import com.google.gson.JsonObject;
import org.aldeon.protocol.query.ExampleQuery;
import org.aldeon.protocol.query.Query;

public class ProtocolClassMapper implements ClassMapper<Query> {
    @Override
    public Class<? extends Query> getClass(JsonObject jsonObject) {

        String action = jsonObject.get("action").getAsString();

        if(action.equals("example")) {
            return ExampleQuery.class;
        }
        return null;
    }
}
