package org.aldeon.communication.http_provider.jetty_receiver;

import com.google.inject.Inject;
import org.aldeon.communication.CommunicationProvider;
import org.aldeon.communication.Receiver;
import org.aldeon.communication.http_provider.IPIdentifier;
import org.aldeon.jetty.resolver.QueryResolver;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  When it receives a message it calls communicationProvider.receive()
 */
public class JettyEndpointHandler extends AbstractHandler {

    private CommunicationProvider<String, IPIdentifier> communicationProvider;

    QueryResolver resolver;

    @Inject
    public JettyEndpointHandler(QueryResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void handle(
            String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException
    {
        log.info("Received request from " + request.getRemoteAddr() + ":" + request.getRemotePort());

        if(request.getMethod() == "GET") {
            try {
                String query = request.getParameter("query");
                respond(resolver.queryResponse(query, observer), response, baseRequest);
                return;
            } catch (Exception e) {
                log.warn("Exception caught when constructing the response", e);
            }
        }
        respond(resolver.errorResponse(), response, baseRequest);
    }

    protected void respond(String answer, HttpServletResponse response, Request baseRequest) throws IOException {
        response.setContentType("text/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println(answer);
    }



}
