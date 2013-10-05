package org.aldeon.jetty;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import org.aldeon.protocol.Protocol;
import org.aldeon.protocol.query.Query;
import org.aldeon.protocol.response.Response;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

class JettyEndpointHandler extends ObserverAwareAbstractHandler {

    private static Logger log = Logger.getLogger(JettyEndpointHandler.class);

    StringQueryResolver resolver;

    @Inject
    public JettyEndpointHandler(StringQueryResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        log.info("Received request (" + request.toString() + ") "
                + "from " + request.getRemoteAddr()
                + ":" + request.getRemotePort());

        if(request.getMethod() == "GET") {
            try {
                respond(resolver.queryResponse(request.getParameter("query"), observer), response, baseRequest);
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
