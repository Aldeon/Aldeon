package org.aldeon.app;

import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.SendPoint;
import org.aldeon.networking.exceptions.UnexpectedAddressClassException;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;
import org.aldeon.networking.mediums.ip.sender.NettySendPoint;
import org.aldeon.utils.helpers.BufPrint;

import java.nio.ByteBuffer;

public class Debug {

    public static void main(String[] args) throws UnexpectedAddressClassException {

        final SendPoint point = new NettySendPoint();

        point.send(new SendPoint.OutgoingTransmission() {
            @Override
            public void onSuccess(ByteBuffer data) {
                System.out.println("Success!");
                System.out.println(BufPrint.hex(data));

                point.close();
            }

            @Override
            public void onFailure(Throwable cause) {
                System.out.println("Failure!");
                System.out.println("Cause: " + cause);

                point.close();
            }

            @Override
            public int timeout() {
                return 5000;
            }

            @Override
            public PeerAddress address() {
                return IpPeerAddress.create("127.0.0.1", 80);
            }

            @Override
            public ByteBuffer data() {
                return ByteBuffer.wrap("hello, world!".getBytes());
            }
        });
    }
}
