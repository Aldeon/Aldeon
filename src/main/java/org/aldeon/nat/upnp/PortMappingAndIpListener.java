package org.aldeon.nat.upnp;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.support.igd.PortMappingListener;
import org.fourthline.cling.support.igd.callback.GetExternalIP;
import org.fourthline.cling.support.igd.callback.PortMappingAdd;
import org.fourthline.cling.support.igd.callback.PortMappingDelete;
import org.fourthline.cling.support.model.PortMapping;
import org.fourthline.cling.transport.spi.NetworkAddressFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.*;


class PortMappingAndIpListener extends DefaultRegistryListener {

    private static final Logger log = LoggerFactory.getLogger(PortMappingAndIpListener.class);
    private final PortMapping portMapping;
    private Map<Service, AddressPair> activeServices;

    public PortMappingAndIpListener(PortMapping portMapping) {
        this.portMapping = portMapping;
        this.activeServices = new HashMap<>();
    }

    @Override
    synchronized public void deviceAdded(Registry registry, Device device) {
        final Service service = discoverConnectionService(device);
        if(service == null) return;

        log.info("Detected device: "+ device.getDisplayString());

        final AddressPair ap = new AddressPair();

        try {
            InetAddress devIp = InetAddress.getByName(device.getDetails().getBaseURL().getHost());
            log.info("Device address: " + devIp);

            NetworkAddressFactory factory = registry.getConfiguration().createNetworkAddressFactory();
            Iterator<NetworkInterface> it = factory.getNetworkInterfaces();
            while(it.hasNext()) {
                try {
                    ap.internalAddress = factory.getLocalAddress(it.next(), false, devIp);
                } catch(IllegalStateException e) {
                    // this is the interface you're looking for <waves a hand>
                }
            }
        } catch (UnknownHostException e) {
            log.warn("Device address is invalid.");
        }


        if(ap.internalAddress == null) {
            log.warn("Could not determine the internal address for the device. Port mapping skipped.");
        } else {
            log.info("Activating port mapping on " + device.getDisplayString());

            portMapping.setInternalClient(ap.getInternalAddress().getHostAddress());

            new PortMappingAdd(service, registry.getUpnpService().getControlPoint(), portMapping) {

                @Override
                public void success(ActionInvocation invocation) {
                    log.info("Added port mapping: " + portMapping);
                    activeServices.put(service, ap);

                    controlPoint.execute(new GetExternalIP(service) {
                        @Override
                        protected void success(String externalIPAddress) {
                            try {
                                activeServices.get(service).externalAddress = InetAddress.getByName(externalIPAddress);
                                log.info("External IP address for this service is " + externalIPAddress);
                            } catch (UnknownHostException e) {
                                log.warn("Obtained invalid external IP address (" + externalIPAddress + ").");
                            }
                        }

                        @Override
                        public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                            log.warn("Failed to obtain an external IP address for this service.");
                        }
                    });
                }

                @Override
                public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                    log.warn("Failed to add port mapping to service " + service + " (reason: " + defaultMsg + ")");
                }

            }.run();
        }
    }

    @Override
    synchronized public void deviceRemoved(Registry registry, Device device) {
        for(Service service: device.findServices()) {
            if(activeServices.containsKey(service)) {
                log.warn("Device disappeared, could not delete a port mapping");
                activeServices.remove(service);
            }
        }
    }

    @Override
    synchronized public void beforeShutdown(Registry registry) {
        final Iterator<Service> it = activeServices.keySet().iterator();

        while(it.hasNext()) {
            final Service service = it.next();

            log.info("Attempting to delete port mapping on service" + service);
            new PortMappingDelete(service, registry.getUpnpService().getControlPoint(), portMapping) {

                @Override
                public void success(ActionInvocation invocation) {
                    log.info("Port mapping deleted from service " + service);
                    it.remove();
                    activeServices.remove(service);
                }

                @Override
                public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                    log.warn("Failed to delete port mapping from service " + service + " (reason: " + defaultMsg + ")");
                }

            }.run();
        }
    }

    private static Service discoverConnectionService(Device device) {
        if (!device.getType().equals(PortMappingListener.IGD_DEVICE_TYPE)) {
            return null;
        }

        Device[] connectionDevices = device.findDevices(PortMappingListener.CONNECTION_DEVICE_TYPE);
        if (connectionDevices.length == 0) {
            log.info("IGD doesn't support '" + PortMappingListener.CONNECTION_DEVICE_TYPE + "': " + device);
            return null;
        }

        Device connectionDevice = connectionDevices[0];
        log.info("Using first discovered WAN connection device: " + connectionDevice);

        Service ipConnectionService = connectionDevice.findService(PortMappingListener.IP_SERVICE_TYPE);
        Service pppConnectionService = connectionDevice.findService(PortMappingListener.PPP_SERVICE_TYPE);

        if (ipConnectionService == null && pppConnectionService == null) {
            log.info("IGD doesn't support IP or PPP WAN connection service: " + device);
        }

        return ipConnectionService != null ? ipConnectionService : pppConnectionService;
    }

    public class AddressPair{
        private InetAddress internalAddress;
        private InetAddress externalAddress;

        public InetAddress getInternalAddress() {
            return internalAddress;
        }

        public InetAddress getExternalAddress() {
            return externalAddress;
        }
    }

    synchronized public Set<AddressPair> getActiveMappings() {
        Set<AddressPair> result = new HashSet<>();
        for(AddressPair ap: activeServices.values()) {
            if(ap.internalAddress != null && ap.externalAddress != null) {
                result.add(ap);
            }
        }
        return result;
    }
}
