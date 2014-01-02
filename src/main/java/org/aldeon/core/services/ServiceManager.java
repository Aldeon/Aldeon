package org.aldeon.core.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class ServiceManager implements Service {

    private static final Logger log = LoggerFactory.getLogger(ServiceManager.class);

    private List<Service> services = new LinkedList<>();

    public void registerService(Service service) {
        services.add(service);
    }

    @Override
    public void start() {
        for(Service service: services) {
            service.start();
        }
    }

    @Override
    public void close() {
        for(Service service: services) {
            service.close();
        }
    }
}
