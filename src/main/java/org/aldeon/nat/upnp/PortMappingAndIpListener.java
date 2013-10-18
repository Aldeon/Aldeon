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
import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class PortMappingAndIpListener extends DefaultRegistryListener {

    private static final Logger log = Logger.getLogger(PortMappingAndIpListener.class.getName());
    private final PortMapping portMapping;
    private Set<Service> servicesWithActivePortMapping;
    private Map<Service, InetAddress> externalIpsByService;

    public PortMappingAndIpListener(PortMapping portMapping) {
        this.portMapping = portMapping;
        servicesWithActivePortMapping = new HashSet<>();
        externalIpsByService = new HashMap<>();
    }

    synchronized public Set<InetAddress> getExternalIPsOfActivePortMappings() {
        return new HashSet(externalIpsByService.values());
    }

    @Override
    synchronized public void deviceAdded(Registry registry, Device device) {

        final Service service = discoverConnectionService(device);
        if(service == null) return;

        log.info("Activating port mapping on" + service);

        new PortMappingAdd(service, registry.getUpnpService().getControlPoint(), portMapping) {

            @Override
            public void success(ActionInvocation invocation) {
                log.info("Added port mapping (" + portMapping +  ") to service " + service);
                servicesWithActivePortMapping.add(service);

                new GetExternalIP(service) {
                    @Override
                    protected void success(String externalIPAddress) {
                        try {
                            externalIpsByService.put(service, InetAddress.getByName(externalIPAddress));
                            log.info("External IP address for this service is " + externalIPAddress);
                        } catch (UnknownHostException e) {
                            log.warn("Obtained invalid external IP address (" + externalIPAddress + ").");
                        }
                    }

                    @Override
                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        log.warn("Failed to obtain an external IP address for this service.");
                    }
                }.run();
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                log.warn("Failed to add port mapping to service " + service + " (reason: " + defaultMsg + ")");
            }

        }.run();
    }

    @Override
    synchronized public void deviceRemoved(Registry registry, Device device) {
        for(Service service: device.findServices()) {
            if(servicesWithActivePortMapping.contains(service)) {
                log.warn("Device disappeared, could not delete a port mapping");
                servicesWithActivePortMapping.remove(service);
                externalIpsByService.remove(service);
            }
        }
    }

    @Override
    synchronized public void beforeShutdown(Registry registry) {

        final Iterator<Service> it = servicesWithActivePortMapping.iterator();

        while(it.hasNext()) {
            final Service service = it.next();

            log.info("Attempting to delete port mapping on service" + service);
            new PortMappingDelete(service, registry.getUpnpService().getControlPoint(), portMapping) {

                @Override
                public void success(ActionInvocation invocation) {
                    log.info("Port mapping deleted from service " + service);
                    it.remove();
                    externalIpsByService.remove(service);
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
}
