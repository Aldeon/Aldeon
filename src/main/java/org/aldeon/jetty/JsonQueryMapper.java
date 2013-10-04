package org.aldeon.jetty;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.aldeon.protocol.query.Query;
import org.aldeon.protocol.query.StatusQuery;

class JsonQueryMapper {

    private Gson gson;

    public JsonQueryMapper() {
        gson = new Gson();
    }

    public Query parse(JsonObject json) throws Exception{

        String action = json.get("action").getAsString();

        if(action.equals("status")) {
            return gson.fromJson(json, StatusQuery.class);
        }

        throw new IllegalArgumentException();
    }
}
