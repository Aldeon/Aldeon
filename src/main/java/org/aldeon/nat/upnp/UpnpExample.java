package org.aldeon.nat.upnp;

import org.aldeon.common.net.AddressTranslation;
import org.aldeon.common.net.Port;
import org.aldeon.common.net.PortImpl;
import org.aldeon.nat.AddressTranslationFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.IOException;
import java.net.InetAddress;

public class UpnpExample {

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        Port internalPort = new PortImpl(80);
        Port externalPort = new PortImpl(12346);
        InetAddress ip = InetAddress.getByName("192.168.1.90");

        AddressTranslationFactory factory = UpnpAddressTranslationFactory.create(internalPort, externalPort, ip);

        System.out.println("Trying to map a port...");

        factory.begin();

        int cycles = 0;

        while(!factory.isReady()) {
            Thread.sleep(100);
            if(++cycles > 50) break;
        }

        AddressTranslation addressTranslation = factory.getAddressTranslation();

        if(addressTranslation == null) {
            System.out.println("No address translation obtained.");
            factory.abort();
            return;
        }

        System.out.println("Success!");

        System.out.println("Internal port : " + addressTranslation.getInternalPort());
        System.out.println("External port : " + addressTranslation.getExternalPort());
        System.out.println("Internal IP   : " + addressTranslation.getInternalAddress());
        System.out.println("External IP   : " + addressTranslation.getExternalAddress());

        System.in.read();

        addressTranslation.shutdown();
    }
}
