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
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JettyEndpointHandlerTest {

    public static final String VALID_QUERY      = "valid_query";
    public static final String VALID_RESPONSE   = "valid_response";
    public static final String ERROR_RESPONSE   = "error_response";


    @Test
    public void shouldUseQueryResolverOnIncomingQueries() throws Exception {
        StringQueryResolver resolver = mock(StringQueryResolver.class);
        Observer observer = mock(Observer.class);

        Request baseRequest = mock(Request.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("query")).thenReturn(VALID_QUERY);
        when(resolver.queryResponse(VALID_QUERY, observer)).thenReturn(VALID_RESPONSE);
        when(response.getWriter()).thenReturn(writer);


        JettyEndpointHandler handler = new JettyEndpointHandler(resolver);
        handler.setObserver(observer);
        handler.handle("target", baseRequest, request, response);

        verify(baseRequest).setHandled(true);
        verify(writer).println(VALID_RESPONSE);
    }

    @Test
    public void shouldReturnErrorForInvalidQuery() throws Exception {
        StringQueryResolver resolver = mock(StringQueryResolver.class);
        Observer observer = mock(Observer.class);

        Request baseRequest = mock(Request.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("query")).thenReturn(VALID_QUERY);
        when(resolver.queryResponse(VALID_QUERY, observer)).thenThrow(new IllegalArgumentException());
        when(resolver.errorResponse()).thenReturn(ERROR_RESPONSE);
        when(response.getWriter()).thenReturn(writer);

        JettyEndpointHandler handler = new JettyEndpointHandler(resolver);
        handler.setObserver(observer);
        handler.handle("target", baseRequest, request, response);

        verify(baseRequest).setHandled(true);
        verify(writer).println(ERROR_RESPONSE);
    }
}
