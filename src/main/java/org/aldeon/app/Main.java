package org.aldeon.app;

import org.aldeon.common.EndpointWithConnectionPolicy;
import org.aldeon.common.net.PortImpl;
import org.aldeon.common.net.ConnectionPolicy;
import org.aldeon.common.net.Port;
import org.aldeon.common.net.StaticConnectionPolicy;
import org.aldeon.jetty.JettyModule;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) throws UnknownHostException {

        Port port = new PortImpl(8080);
        InetAddress address = InetAddress.getLocalHost();
        ConnectionPolicy policy = new StaticConnectionPolicy(port, port, address, address);

        EndpointWithConnectionPolicy endpoint = JettyModule.getEndpoint();
        endpoint.setObserver(new FooObserver());
        endpoint.setConnectionPolicy(policy);
        endpoint.start();

        try {
            System.in.read();
        } catch (IOException e) {}

        endpoint.stop();
    }

}
