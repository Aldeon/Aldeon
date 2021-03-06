package org.aldeon.networking.mediums.ip.nat.upnp;

import org.aldeon.networking.mediums.ip.nat.utils.AddressTranslation;
import org.aldeon.networking.common.Port;
import org.aldeon.utils.net.PortImpl;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

        try {
            AddressTranslation addressTranslation = future.get(5000, TimeUnit.MILLISECONDS);

            System.out.println("Success!");

            System.out.println("Internal port : " + addressTranslation.getInternalPort());
            System.out.println("External port : " + addressTranslation.getExternalPort());
            System.out.println("Internal IP   : " + addressTranslation.getInternalAddress());
            System.out.println("External IP   : " + addressTranslation.getExternalAddress());

            System.in.read();

            System.out.println("Shutting down...");

            addressTranslation.shutdown();

        } catch (TimeoutException e) {
            System.out.println("No address translation obtained.");
            future.cancel(true);
            return;
        }
    }
}
