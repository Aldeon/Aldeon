package org.aldeon.jetty;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import org.aldeon.common.Observer;
import org.aldeon.protocol.Protocol;
import org.aldeon.protocol.query.Query;
import org.aldeon.protocol.response.Response;

public class StringQueryResolver {

    JsonParser parser;
    JsonQueryMapper mapper;
    Protocol protocol;
    Gson gson;

    @Inject
    public StringQueryResolver(JsonParser parser, JsonQueryMapper mapper, Protocol protocol, Gson gson) {
        this.parser = parser;
        this.mapper = mapper;
        this.protocol = protocol;
        this.gson = gson;
    }

    public String errorResponse() {
        return "{\"error\":\"true\"}";
    }

    public String queryResponse(String query, Observer observer) throws Exception{

        // First, parse the query passed in the http request.
        JsonObject jsonQuery = (JsonObject) parser.parse(query);

        // Great, we have a json. Now let's convert it into a protocol query.
        Query protocolQuery = mapper.parse(jsonQuery);

        // We have a proper query. We use our protocol to fetch the response.
        Response protocolResponse = protocol.respond(protocolQuery, observer);

        return gson.toJson(protocolResponse);
    }
}
