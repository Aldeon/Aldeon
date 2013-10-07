package org.aldeon.jetty;

import com.google.inject.Inject;
import org.aldeon.common.EndpointWithPortPolicy;
import org.aldeon.common.Observer;
import org.aldeon.common.net.PortPolicy;
import org.aldeon.jetty.handler.ObserverAwareAbstractHandler;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;


class JettyHttpEndpoint implements EndpointWithPortPolicy {

    private static Logger log = Logger.getLogger(JettyHttpEndpoint.class);

    private PortPolicy portPolicy;
    private Server server;
    private ObserverAwareAbstractHandler handler;
    private ServerThread thread;

    @Inject
    public JettyHttpEndpoint(ObserverAwareAbstractHandler handler) {
        this.handler = handler;
    }

    @Override
    public void setObserver(Observer observer) {
        handler.setObserver(observer);
    }

    @Override
    public void setPortPolicy(PortPolicy portPolicy) {
        this.portPolicy = portPolicy;
    }

    @Override
    public PortPolicy getPortPolicy() {
        return portPolicy;
    }

    @Override
    public void start() {
        int port = getPortPolicy().getInternalPort().getIntValue();
        server = new Server(port);
        server.setHandler(this.handler);
        thread = new ServerThread();
        thread.start();
        log.info("Started Jetty endpoint on port " + port);
    }

    @Override
    public void stop() {
        try {
            server.stop();
            log.info("Stopped Jetty endpoint");
        } catch (Exception e) {
            log.error("Failed to stop Jetty endpoint", e);
        }
    }

    private class ServerThread extends Thread {
        @Override
        public void run() {
            try {
                server.start();
                server.join();
            } catch (Exception e) {
                log.error("Failed to start Jetty endpoint", e);
            }
        }
    }
}
