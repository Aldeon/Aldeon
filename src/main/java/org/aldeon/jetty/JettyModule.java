package org.aldeon.jetty;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.aldeon.common.EndpointWithPortPolicy;
import org.aldeon.protocol.Protocol;
import org.aldeon.protocol.TestProtocol;

public class JettyModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(EndpointWithPortPolicy.class).to(JettyHttpEndpoint.class);
        bind(ObserverAwareAbstractHandler.class).to(JettyEndpointHandler.class);
        bind(Protocol.class).to(TestProtocol.class);
    }

    public static EndpointWithPortPolicy getEndpoint() {
        Injector jettyInjector = Guice.createInjector(new JettyModule());
        return jettyInjector.getInstance(EndpointWithPortPolicy.class);
    }
}
