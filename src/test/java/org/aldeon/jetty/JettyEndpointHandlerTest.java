package org.aldeon.jetty;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.aldeon.common.Observer;
import org.aldeon.protocol.Protocol;
import org.aldeon.protocol.query.Query;
import org.aldeon.protocol.response.Response;
import org.eclipse.jetty.server.Request;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JettyEndpointHandlerTest {

    public static final String VALID_QUERY      = "valid_query";
    public static final String VALID_RESPONSE   = "valid_response";


    // @Test WORK IN PROGRESS
    public void shouldReturnValidResponseForValidQuery() throws Exception {

        JsonQueryMapper mapper = mock(JsonQueryMapper.class);
        JsonParser parser = mock(JsonParser.class);
        Protocol protocol = mock(Protocol.class);
        Observer observer = mock(Observer.class);
        Gson gson = mock(Gson.class);

        Request baseRequest = mock(Request.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        JsonObject json = mock(JsonObject.class);
        Query query = mock(Query.class);
        Response resp = mock(Response.class);
        PrintWriter writer = mock(PrintWriter.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("query")).thenReturn(VALID_QUERY);
        when(parser.parse(VALID_QUERY)).thenReturn(json);
        when(mapper.parse(json)).thenReturn(query);
        when(protocol.respond(query, observer)).thenReturn(resp);
        when(gson.toJson(resp)).thenReturn(VALID_RESPONSE);
        when(response.getWriter()).thenReturn(writer);


        JettyEndpointHandler handler = new JettyEndpointHandler(mapper, parser, protocol, gson);
        handler.setObserver(observer);
        handler.handle("target", baseRequest, request, response);

        verify(baseRequest).setHandled(true);
        verify(writer).println(VALID_RESPONSE);
    }
}
