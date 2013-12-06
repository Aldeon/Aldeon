package org.aldeon.app;

import org.aldeon.communication.Sender;
import org.aldeon.communication.netty.NettySenderModule;
import org.aldeon.communication.task.OutboundRequestTask;
import org.aldeon.net.Ipv4PeerAddress;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetRelevantPeersRequest;
import org.aldeon.utils.helpers.Identifiers;

public class Debug {

    public static void main(String[] args) {

        final Sender sender = new NettySenderModule().get();

        sender.addTask(new OutboundRequestTask() {
            @Override
            public void onSuccess(Response response) {
                System.out.println("Success! " + response);
                sender.close();
            }

            @Override
            public void onFailure(Throwable cause) {
                System.out.println("Fail! " + cause);
                sender.close();
            }

            @Override
            public int getTimeoutMillis() {
                return 5000;
            }

            @Override
            public Request getRequest() {

                GetRelevantPeersRequest req = new GetRelevantPeersRequest();

                req.target = Identifiers.fromBase64("Fzq.9u1gFMhF6C6Evfmyo46gQ.pC9yQ3MU0dY50.8t0-");

                return req;
            }

            @Override
            public PeerAddress getAddress() {
                return Ipv4PeerAddress.parse("192.168.1.103", 8080);
            }
        });

    }


}
