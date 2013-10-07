package org.aldeon.jetty.resolver;

import org.aldeon.common.Observer;
import org.aldeon.jetty.json.ClassMapper;
import org.aldeon.jetty.json.JsonParser;
import org.aldeon.jetty.json.ParseException;
import org.aldeon.protocol.Protocol;
import org.aldeon.protocol.query.Query;
import org.aldeon.protocol.response.Response;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonQueryResolverTest {

    @Test
    public void shouldParseToJsonAndAskProtocolForResponse() throws InvalidQueryException, ParseException {

        String query = "some_query";
        String expectedResponse = "expected_response";

        Protocol protocol = mock(Protocol.class);
        JsonParser parser = mock(JsonParser.class);
        Observer observer = mock(Observer.class);
        Query queryObject = mock(Query.class);
        Response responseObject = mock(Response.class);
        ClassMapper<Query> mapper = mock(ClassMapper.class);


        when(parser.fromJson(query, mapper)).thenReturn(queryObject);
        when(parser.toJson(responseObject)).thenReturn(expectedResponse);
        when(protocol.respond(queryObject, observer)).thenReturn(responseObject);

        JsonQueryResolver resolver = new JsonQueryResolver(protocol, parser, mapper);

        String response = resolver.queryResponse(query, observer);
        assertEquals(expectedResponse, response);
    }

    @Test(expected = InvalidQueryException.class)
    public void shouldThrowInvalidQueryExceptionWhenParsingFails() throws ParseException, InvalidQueryException {

        String query = "some_query";

        Protocol protocol = mock(Protocol.class);
        JsonParser parser = mock(JsonParser.class);
        Observer observer = mock(Observer.class);
        ClassMapper<Query> mapper = mock(ClassMapper.class);

        when(parser.fromJson(query, mapper)).thenThrow(new ParseException());

        JsonQueryResolver resolver = new JsonQueryResolver(protocol, parser, mapper);
        resolver.queryResponse(query, observer);
    }
}
