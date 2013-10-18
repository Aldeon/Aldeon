package org.aldeon.nat.upnp;

import org.aldeon.common.net.ConnectionPolicy;
import org.aldeon.common.net.Port;
import org.aldeon.nat.ConnectionPolicyFactory;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.model.types.UnsignedIntegerTwoBytes;
import org.fourthline.cling.support.model.PortMapping;

import java.net.InetAddress;
import java.util.Set;

public class UpnpPolicyFactory implements ConnectionPolicyFactory {

    private static int LEASE_DURATION = 0;
    private Port desiredInternalPort;
    private Port desiredExternalPort;
    private InetAddress internalAddress;
    private PortMappingAndIpListener listener;
    private UpnpService upnp;
    private boolean didShutDown = false;


    private UpnpPolicyFactory() {};

    public static UpnpPolicyFactory create(Port desiredInternalPort, Port desiredExternalPort, InetAddress internalAddress) {

        PortMapping pm = new PortMapping();
        pm.setDescription("Aldeon UPnP TCP");
        pm.setProtocol(PortMapping.Protocol.TCP);
        pm.setLeaseDurationSeconds(new UnsignedIntegerFourBytes(LEASE_DURATION));
        pm.setInternalPort(new UnsignedIntegerTwoBytes(desiredInternalPort.getIntValue()));
        pm.setExternalPort(new UnsignedIntegerTwoBytes(desiredExternalPort.getIntValue()));
        pm.setInternalClient(internalAddress.getHostAddress());

        UpnpPolicyFactory factory = new UpnpPolicyFactory();

        factory.listener = new PortMappingAndIpListener(pm);
        factory.upnp = new UpnpServiceImpl(factory.listener);

        factory.desiredInternalPort = desiredInternalPort;
        factory.desiredExternalPort = desiredExternalPort;
        factory.internalAddress = internalAddress;

        return factory;
    }

    @Override
    public void begin() {
        upnp.getControlPoint().search();
    }

    @Override
    public void abort() {
        if(!didShutDown) {
            upnp.shutdown();
            didShutDown = true;
        }
    }

    @Override
    public boolean isReady() {
        return listener.getExternalIPsOfActivePortMappings().size() > 0;
    }

    @Override
    public boolean didFail() {
        return didShutDown;
    }

    @Override
    public ConnectionPolicy getPolicy() {
        Set<InetAddress> addresses = listener.getExternalIPsOfActivePortMappings();
        if(addresses.size() == 0) return null;

        final InetAddress address = addresses.iterator().next(); //we take only the first one

        return new ConnectionPolicy() {
            @Override
            public Port getInternalPort() {
                return desiredInternalPort;
            }

            @Override
            public Port getExternalPort() {
                return desiredExternalPort;
            }

            @Override
            public InetAddress getInternalAddress() {
                return internalAddress;
            }

            @Override
            public InetAddress getExternalAddress() {
                return address;
            }

            @Override
            public void close() {
                abort();
            }
        };
    }
}
