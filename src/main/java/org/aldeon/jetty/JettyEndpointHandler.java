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
    private JsonQueryMapper mapper;
    private Protocol protocol;
    private JsonParser parser;
    private Gson gson;

    @Inject
    public JettyEndpointHandler(JsonQueryMapper mapper, JsonParser parser, Protocol protocol, Gson gson) {
        this.mapper = mapper;
        this.protocol = protocol;
        this.parser = parser;
        this.gson = gson;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        log.info("Received request (" + request.toString() + ") from " + request.getRemoteAddr() + ":" + request.getRemotePort());

        if(request.getMethod() == "GET") {
            try {
                // First, parse the query passed in the http request.
                JsonObject jsonQuery = (JsonObject) parser.parse(request.getParameter("query"));

                // Great, we have a json. Now let's convert it into a protocol query.
                Query protocolQuery = mapper.parse(jsonQuery);

                // We have a proper query. We use our protocol to fetch the response.
                Response protocolResponse = protocol.respond(protocolQuery, observer);

                // Send it to client.
                respond(gson.toJson(protocolResponse), response, baseRequest);

            } catch(Exception e) {
                // debug
                respond("{\"error\": \"" + e.getMessage() + "\"}", response, baseRequest);
                e.printStackTrace();
            }
        } else {
            // debug
            respond("{\"error\": \"asdsdsas\"}", response, baseRequest);
        }

    }

    protected void respond(String answer, HttpServletResponse response, Request baseRequest) throws IOException {
        response.setContentType("text/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println(answer);
    }
}
