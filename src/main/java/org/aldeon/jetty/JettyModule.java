package org.aldeon.jetty;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.aldeon.common.EndpointWithPortPolicy;

public class JettyModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(EndpointWithPortPolicy.class).to(JettyHttpEndpoint.class);
        bind(ResponderAwareAbstractHandler.class).to(JettyEndpointHandler.class);
    }

    public static EndpointWithPortPolicy getEndpoint() {
        Injector jettyInjector = Guice.createInjector(new JettyModule());
        return jettyInjector.getInstance(EndpointWithPortPolicy.class);
    }
}
