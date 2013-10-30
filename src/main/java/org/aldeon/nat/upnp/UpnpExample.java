package org.aldeon.nat.upnp;

import org.aldeon.net.AddressTranslation;
import org.aldeon.net.Port;
import org.aldeon.utils.net.PortImpl;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class UpnpExample {

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

        Port internalPort = new PortImpl(80);
        Port externalPort = new PortImpl(12348);

        Future<AddressTranslation> future = UpnpAddressTranslationFactory.create(internalPort, externalPort);

        System.out.println("Trying to map a port...");

        int cycles = 0;

        while(!future.isDone()) {
            Thread.sleep(100);
            if(++cycles > 50) break;
        }

        AddressTranslation addressTranslation = future.get();

        if(addressTranslation == null) {
            System.out.println("No address translation obtained.");
            future.cancel(true);
            return;
        }

        System.out.println("Success!");

        System.out.println("Internal port : " + addressTranslation.getInternalPort());
        System.out.println("External port : " + addressTranslation.getExternalPort());
        System.out.println("Internal IP   : " + addressTranslation.getInternalAddress());
        System.out.println("External IP   : " + addressTranslation.getExternalAddress());

        System.in.read();

        System.out.println("Shutting down...");

        addressTranslation.shutdown();
    }
}
