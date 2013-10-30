package org.aldeon.protocol;


import org.aldeon.common.core.Core;
import org.aldeon.protocol.example.ExampleDateResponse;
import org.aldeon.utils.various.Callback;

public class ProtocolImpl implements Protocol {

    private final Core core;

    public ProtocolImpl(Core core) {
        this.core = core;
    }

    @Override
    public void createResponse(Request request, final Callback<Response> onResponse) {

        /* --- we are inside the core.getServerSideExecutor() --- */

        /*
             We have access to the application core

             core.getStorage()
             core.getEventLoop()
             core.getSender(addressType)

             ... and more.

             TODO implement all classes in org.aldeon.communication.converter package

         */

        // Here we (eventually, somehow) generate the response
        final Response response = new ExampleDateResponse();

        // Here we send the response
        onResponse.call(response);
    }
}
