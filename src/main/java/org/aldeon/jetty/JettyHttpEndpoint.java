package org.aldeon.jetty;

import com.google.inject.Inject;
import org.aldeon.common.EndpointWithConnectionPolicy;
import org.aldeon.common.Observer;
import org.aldeon.common.net.ConnectionPolicy;
import org.aldeon.jetty.handler.ObserverAwareAbstractHandler;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class JettyHttpEndpoint implements EndpointWithConnectionPolicy {

    private static final Logger log = LoggerFactory.getLogger(JettyHttpEndpoint.class);

    private ConnectionPolicy connectionPolicy;
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
    public void setConnectionPolicy(ConnectionPolicy connectionPolicy) {
        this.connectionPolicy = connectionPolicy;
    }

    @Override
    public ConnectionPolicy getConnectionPolicy() {
        return connectionPolicy;
    }

    @Override
    public void start() {
        int port = getConnectionPolicy().getInternalPort().getIntValue();
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
            connectionPolicy.close();
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
