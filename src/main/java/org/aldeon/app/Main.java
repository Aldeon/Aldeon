package org.aldeon.app;

import org.aldeon.common.EndpointWithPortPolicy;
import org.aldeon.common.net.ConcretePort;
import org.aldeon.common.net.StaticPortPolicy;
import org.aldeon.jetty.JettyModule;
import org.aldeon.utils.config.Logging;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Logging.configure();

        EndpointWithPortPolicy endpoint = JettyModule.getEndpoint();
        endpoint.setObserver(new FooObserver());
        endpoint.setPortPolicy(new StaticPortPolicy(new ConcretePort(8080), new ConcretePort(8080)));
        endpoint.start();

        System.out.println("Press any key to exit");
        try {
            System.in.read();
        } catch (IOException e) {

        }

        endpoint.stop();

    }

}
