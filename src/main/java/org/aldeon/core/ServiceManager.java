package org.aldeon.core;

import org.aldeon.model.Service;

import java.util.LinkedList;
import java.util.List;

public class ServiceManager implements Service {

    private List<Service> services = new LinkedList<>();

    public void register(Service service) {
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
