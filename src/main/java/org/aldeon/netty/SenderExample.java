package org.aldeon.netty;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.aldeon.common.net.Port;
import org.aldeon.utils.net.PortImpl;
import org.aldeon.common.net.address.IpPeerAddress;
import org.aldeon.communication.Sender;
import org.aldeon.netty.converter.ProtocolRequestEncoder;
import org.aldeon.netty.converter.ProtocolResponseDecoder;
import org.aldeon.netty.sender.NettySender;
import org.aldeon.netty.sender.RequestListener;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.Converter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SenderExample {
    public static void main(String[] args) throws IOException, InterruptedException {

        // We create a thread pool where all the Listeners are executed.
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Conversion objects
        Converter<Request, FullHttpRequest>   encoder = new ProtocolRequestEncoder();
        Converter<FullHttpResponse, Response> decoder = new ProtocolResponseDecoder();

        // Sender object
        Sender<IpPeerAddress> sender = new NettySender(encoder, decoder);

        // Let's create a request
        Request request = null;

        // Who are we sending the request to?
        IpPeerAddress address = new SomeAddress("123.123.123.123", 8080);

        // Send the request and register a Listener
        sender.addTask(new RequestListener(executor, request, address) {
            @Override
            public void onSuccess(Response response) {
                System.out.println("Response received:");
                System.out.println(response);
            }

            @Override
            public void onFailure(Throwable cause) {
                System.out.println("Request failed (" + cause.getClass() + ")");
                System.out.println(cause.getMessage());
            }
        });

        // addTask() does NOT block, so this line will be executed before the response.
        System.out.println("Press any key to exit...");

        System.in.read();
        System.out.println("Stopping NettySender...");
        sender.close();
        executor.shutdown();
    }

    /**
     * Example implementation of IpPeerAddress.
     * IpPeerAddress is an interface for peers reachable through an IP address and port.
     */
    public static class SomeAddress implements IpPeerAddress {

        private InetAddress host;
        private Port port;

        public SomeAddress(String host, int port) throws UnknownHostException {
            this.host = InetAddress.getByName(host);
            this.port = new PortImpl(port);
        }

        @Override
        public Port getPort() {
            return port;
        }

        @Override
        public InetAddress getHost() {
            return host;
        }
    }
}
