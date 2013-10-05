package org.aldeon.app;

import org.aldeon.common.EndpointWithPortPolicy;
import org.aldeon.common.net.ConcretePort;
import org.aldeon.common.net.StaticPortPolicy;
import org.aldeon.jetty.JettyModule;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        EndpointWithPortPolicy endpoint = JettyModule.getEndpoint();
        endpoint.setObserver(new FooObserver());
        endpoint.setPortPolicy(new StaticPortPolicy(new ConcretePort(8080), new ConcretePort(8080)));
        endpoint.start();

        try {
            System.in.read();
        } catch (IOException e) {}

        endpoint.stop();
    }

}
