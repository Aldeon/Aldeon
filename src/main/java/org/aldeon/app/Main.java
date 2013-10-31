package org.aldeon.app;

import org.aldeon.core.Core;
import org.aldeon.net.AddressTranslation;
import org.aldeon.net.IpPeerAddress;
import org.aldeon.protocol.Protocol;
import org.aldeon.communication.netty.NettyModule;
import org.aldeon.core.CoreModule;
import org.aldeon.core.events.AppClosingEvent;
import org.aldeon.nat.utils.NoAddressTranslation;
import org.aldeon.protocol.ProtocolModule;
import org.aldeon.utils.net.PortImpl;

import java.io.IOException;
import java.net.InetAddress;

public class Main {

    public static void main(String[] args) throws IOException {

        // Instantiate the core
        Core core = CoreModule.createCore();

        // Instantiate the protocol
        Protocol protocol = ProtocolModule.createProtocol(core);

        // Decide how to translate addresses (DEBUG: UPnP disabled for now)
        AddressTranslation translation = new NoAddressTranslation(new PortImpl(8080), InetAddress.getByName("0.0.0.0"));

        // Register all senders and receivers we have implemented
        core.registerSender(IpPeerAddress.class, NettyModule.createSender());
        core.registerReceiver(IpPeerAddress.class, NettyModule.createReceiver(translation));

        core.initSenders();
        core.initReceivers(protocol);

        /*
            Now everything should work.
            To see the results, go to:

            http://localhost:8080?query={"type":"get_message","id":"CaKjxIm3DbrEmeCsso5hFX8AyagHBrv1UBiSrpN8vjE-"}
         */


        // Core awaits for the AppClosingEvent to occur. Then it will close.

        // This should actually be called in GUI
        System.out.println("Press any key to close...");
        System.in.read();
        core.getEventLoop().notify(new AppClosingEvent());
    }

}
