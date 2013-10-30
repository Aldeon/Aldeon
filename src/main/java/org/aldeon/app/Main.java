package org.aldeon.app;

import org.aldeon.common.core.Core;
import org.aldeon.common.net.AddressTranslation;
import org.aldeon.common.net.address.IpPeerAddress;
import org.aldeon.communication.netty.NettyModule;
import org.aldeon.core.CoreModule;
import org.aldeon.events.AppClosingEvent;
import org.aldeon.nat.utils.NoAddressTranslation;
import org.aldeon.protocol.Protocol;
import org.aldeon.protocol.ProtocolImpl;
import org.aldeon.utils.net.PortImpl;

import java.io.IOException;
import java.net.InetAddress;

public class Main {

    public static void main(String[] args) throws IOException {

        // Instantiate the core
        Core core = CoreModule.createCore();

        // Instantiate the protocol
        Protocol protocol = new ProtocolImpl(core);

        // Decide how to translate addresses (DEBUG: UPnP disabled for now)
        AddressTranslation translation = new NoAddressTranslation(new PortImpl(8080), InetAddress.getLocalHost());

        // Register all senders and receivers we have implemented
        core.registerSender(IpPeerAddress.class, NettyModule.createSender());
        core.registerReceiver(IpPeerAddress.class, NettyModule.createReceiver(translation));

        core.initSenders();
        core.initReceivers(protocol);

        // Core awaits the AppClosingEvent to occur. Then it will close.

        // This should actually be called in GUI
        System.out.println("Press any key to close...");
        System.in.read();
        core.getEventLoop().notify(new AppClosingEvent());
    }

}
