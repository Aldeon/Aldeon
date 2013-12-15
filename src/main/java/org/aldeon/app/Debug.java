package org.aldeon.app;

import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.core.events.AppClosingEvent;
import org.aldeon.networking.common.OutboundRequestTask;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.Sender;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetMessageRequest;
import org.aldeon.protocol.response.MessageFoundResponse;
import org.aldeon.utils.helpers.Identifiers;

public class Debug {
    public static void main(String[] args) {

        Core core = CoreModule.getInstance();

        Sender sender = core.getSender();

        sender.addTask(new OutboundRequestTask() {
            @Override
            public void onSuccess(Response response) {
                System.out.println("Message received!!!1");
                System.out.println("Type: " + response.getClass());

                if(response instanceof MessageFoundResponse) {
                    System.out.println("Our message is: ");
                    System.out.println(((MessageFoundResponse) response).message);
                }
                close();
            }

            @Override
            public void onFailure(Throwable cause) {
                System.out.println("Something went terribly wrong");
                System.out.println(cause);
                close();
            }

            @Override
            public int getTimeoutMillis() {
                return 5000;
            }

            @Override
            public Request getRequest() {
                GetMessageRequest req = new GetMessageRequest();
                req.id = Identifiers.fromBase64("9HCiMrH/0qcwkKhxOtHWjOwkkqGAcpJBp7OCD5jFAkM-");
                return req;
            }

            @Override
            public PeerAddress getAddress() {
                return IpPeerAddress.create("192.168.1.42", 41530);
            }
        });

        //
    }

    public static void close() {
        CoreModule.getInstance().getEventLoop().notify(new AppClosingEvent());
    }
}
