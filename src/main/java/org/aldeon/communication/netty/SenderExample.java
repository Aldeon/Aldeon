package org.aldeon.communication.netty;

import org.aldeon.communication.Sender;
import org.aldeon.communication.netty.sender.RequestListener;
import org.aldeon.net.IpPeerAddress;
import org.aldeon.net.Ipv4PeerAddress;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SenderExample {
    public static void main(String[] args) throws IOException, InterruptedException {

        // We create a thread pool where all the Listeners are executed.
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Sender object
        Sender<IpPeerAddress> sender = NettyModule.createSender(IpPeerAddress.class);

        // Let's create a request
        Request request = null;

        // Who are we sending the request to?
        IpPeerAddress address = Ipv4PeerAddress.parse("123.123.123.123", 8080);

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

}
