package org.aldeon.nat.upnp;

import org.aldeon.common.net.AddressTranslation;
import org.aldeon.common.net.Port;
import org.aldeon.nat.AddressTranslationFactory;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.model.types.UnsignedIntegerTwoBytes;
import org.fourthline.cling.support.model.PortMapping;

import java.net.InetAddress;
import java.util.Set;

public class UpnpAddressTranslationFactory implements AddressTranslationFactory {

    private static final int LEASE_DURATION_SECONDS = 86400; //debug
    private Port desiredInternalPort;
    private Port desiredExternalPort;
    private PortMappingAndIpListener listener;
    private UpnpService upnp;
    private boolean didShutDown = false;


    private UpnpAddressTranslationFactory() {}

    public static UpnpAddressTranslationFactory create(Port desiredInternalPort, Port desiredExternalPort) {

        PortMapping pm = new PortMapping();
        pm.setDescription("Aldeon UPnP TCP");
        pm.setProtocol(PortMapping.Protocol.TCP);
        pm.setLeaseDurationSeconds(new UnsignedIntegerFourBytes(LEASE_DURATION_SECONDS));
        pm.setInternalPort(new UnsignedIntegerTwoBytes(desiredInternalPort.getIntValue()));
        pm.setExternalPort(new UnsignedIntegerTwoBytes(desiredExternalPort.getIntValue()));

        UpnpAddressTranslationFactory factory = new UpnpAddressTranslationFactory();

        factory.listener = new PortMappingAndIpListener(pm);
        factory.upnp = new UpnpServiceImpl(factory.listener);

        factory.desiredInternalPort = desiredInternalPort;
        factory.desiredExternalPort = desiredExternalPort;

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
        return listener.getActiveMappings().size() > 0;
    }

    @Override
    public AddressTranslation getAddressTranslation() {
        Set<PortMappingAndIpListener.AddressPair> addresses = listener.getActiveMappings();
        if(addresses.size() == 0) return null;

        final PortMappingAndIpListener.AddressPair addressPair = addresses.iterator().next(); //we take only the first one

        return new AddressTranslation() {
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
                return addressPair.getInternalAddress();
            }

            @Override
            public InetAddress getExternalAddress() {
                return addressPair.getExternalAddress();
            }

            @Override
            public void shutdown() {
                abort();
            }
        };
    }
}
