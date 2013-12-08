package org.aldeon.app;

import org.aldeon.events.Callback;
import org.aldeon.networking.common.RecvPoint;
import org.aldeon.networking.mediums.ip.addresses.NewIpPeerAddress;
import org.aldeon.networking.mediums.ip.receiver.NettyRecvPoint;
import org.aldeon.utils.helpers.BufPrint;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Debug {

    public static void main(String[] args) throws IOException {

        NewIpPeerAddress loopback = NewIpPeerAddress.create("0.0.0.0", 8080);

        RecvPoint recv = new NettyRecvPoint(loopback);

        recv.onIncomingTransmission(new Callback<RecvPoint.IncomingTransmission>() {
            @Override
            public void call(RecvPoint.IncomingTransmission val) {

                System.out.println("Received: ");
                System.out.println(BufPrint.hex(val.data()));

                val.respond(ByteBuffer.wrap("{\"lol\":\"true\"}".getBytes()));
            }
        });

        recv.start();
        System.out.println("Press any key...");
        System.in.read();
        recv.close();

    }
}
