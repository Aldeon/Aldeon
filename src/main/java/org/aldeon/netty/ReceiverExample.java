package org.aldeon.netty;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.aldeon.common.net.AddressTranslation;
import org.aldeon.common.net.PortImpl;
import org.aldeon.common.nio.task.InboundRequestTask;
import org.aldeon.nat.utils.NoAddressTranslation;
import org.aldeon.netty.example_converters.ExampleRequestDecoder;
import org.aldeon.netty.example_converters.ExampleResponseEncoder;
import org.aldeon.netty.receiver.NettyReceiver;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.example.ExampleDateRequest;
import org.aldeon.protocol.example.ExampleDateResponse;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.various.Callback;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReceiverExample {
    public static void main(String[] args) throws IOException, InterruptedException {

        // Thread pool responsible for handling incoming requests
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // First, let's decide where to bind the service
        AddressTranslation translation = new NoAddressTranslation(new PortImpl(8080), InetAddress.getByName("0.0.0.0"));

        // Converters
        // Converter<FullHttpRequest, Request>   decoder = new ProtocolRequestDecoder();
        // Converter<Response, FullHttpResponse> encoder = new ProtocolResponseEncoder();
        Converter<FullHttpRequest, Request>   decoder = new ExampleRequestDecoder();
        Converter<Response, FullHttpResponse> encoder = new ExampleResponseEncoder();

        // This is our receiver instance
        final NettyReceiver receiver = new NettyReceiver(translation, executor, encoder, decoder);

        receiver.setCallback(new Callback<InboundRequestTask>() {
            @Override
            public void call(InboundRequestTask task) {

                // This is a request sent to us by a peer
                Request request = task.getRequest();

                // This is our response
                Response response;
                if(request instanceof ExampleDateRequest)
                    response = new ExampleDateResponse();
                else
                    response = null;

                // Let's send the response.
                task.sendResponse(response);

                // We could also discard the response.
                // task.discard();
            }
        });

        // Let's start the service
        receiver.start();

        System.out.println("Press any key to exit...");

        System.in.read();
        System.out.println("Stopping NettyReceiver...");
        receiver.close();
        executor.shutdown();
    }
}
