package org.aldeon.app;

import org.aldeon.common.EndpointWithPortPolicy;
import org.aldeon.common.net.ConcretePort;
import org.aldeon.common.net.StaticPortPolicy;
import org.aldeon.jetty.JettyModule;
import org.aldeon.utils.config.Logging;

public class Main {

    public static void main(String[] args) {
        Logging.configure();

        EndpointWithPortPolicy endpoint = JettyModule.getEndpoint();
        endpoint.setResponder(new FooResponder());
        endpoint.setPortPolicy(new StaticPortPolicy(new ConcretePort(8080), new ConcretePort(8080)));
        endpoint.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        endpoint.stop();

    }

}
