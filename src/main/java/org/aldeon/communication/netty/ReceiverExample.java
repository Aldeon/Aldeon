package org.aldeon.communication.netty;

import org.aldeon.communication.Receiver;
import org.aldeon.communication.task.InboundRequestTask;
import org.aldeon.events.ACB;
import org.aldeon.nat.utils.NoAddressTranslation;
import org.aldeon.net.AddressTranslation;
import org.aldeon.net.IpPeerAddress;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.utils.net.PortImpl;

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

        // This is our receiver instance
        final Receiver<IpPeerAddress> receiver = NettyModule.createReceiver(translation);

        receiver.setCallback(new ACB<InboundRequestTask<IpPeerAddress>>(executor) {
            @Override
            public void react(InboundRequestTask<IpPeerAddress> task) {

                // This is a request sent to us by a peer
                Request request = task.getRequest();

                // This is our response
                Response response = null;

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
