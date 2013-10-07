package org.aldeon.jetty.resolver;

import com.google.inject.Inject;
import org.aldeon.common.Observer;
import org.aldeon.jetty.json.ClassMapper;
import org.aldeon.jetty.json.JsonParser;
import org.aldeon.jetty.json.ParseException;
import org.aldeon.jetty.resolver.QueryResolver;
import org.aldeon.protocol.Protocol;
import org.aldeon.protocol.query.Query;
import org.aldeon.protocol.response.Response;

public class JsonQueryResolver implements QueryResolver{

    ClassMapper<Query> mapper;
    Protocol protocol;
    JsonParser parser;

    @Inject
    public JsonQueryResolver(Protocol protocol, JsonParser parser, ClassMapper<Query> mapper) {
        this.mapper = mapper;
        this.protocol = protocol;
        this.parser = parser;
    }

    @Override
    public String errorResponse() {
        return "{\"error\":\"true\"}";
    }

    @Override
    public String queryResponse(String query, Observer observer) throws InvalidQueryException {
        try {
            Query protocolQuery = parser.fromJson(query, mapper);
            Response protocolResponse = protocol.respond(protocolQuery, observer);
            return parser.toJson(protocolResponse);
        } catch (ParseException e) {
            throw new InvalidQueryException();
        }
    }
}
