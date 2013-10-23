package org.aldeon.app;

import org.aldeon.common.EndpointWithAddressTranslation;
import org.aldeon.common.net.PortImpl;
import org.aldeon.common.net.AddressTranslation;
import org.aldeon.common.net.Port;
import org.aldeon.nat.utils.NoAddressTranslation;
import org.aldeon.nat.utils.StaticAddressTranslation;
import org.aldeon.jetty.JettyModule;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) throws UnknownHostException {

        Port port = new PortImpl(8080);
        InetAddress address = InetAddress.getLocalHost();
        AddressTranslation addressTranslation = new NoAddressTranslation(port, address);

        EndpointWithAddressTranslation endpoint = JettyModule.getEndpoint();
        endpoint.setObserver(new FooObserver());
        endpoint.setAddressTranslation(addressTranslation);
        endpoint.start();

        try {
            System.in.read();
        } catch (IOException e) {}

        endpoint.stop();
    }

}
