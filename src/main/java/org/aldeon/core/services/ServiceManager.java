package org.aldeon.core.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ServiceManager implements Service {

    private static final Logger log = LoggerFactory.getLogger(ServiceManager.class);

    private LinkedList<Service> services = new LinkedList<>();

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
        Iterator<Service> it = services.descendingIterator();
        while(it.hasNext()) {
            it.next().close();
        }
    }
}
