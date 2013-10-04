package org.aldeon.jetty;

import org.aldeon.common.Observer;
import org.eclipse.jetty.server.handler.AbstractHandler;

abstract class ObserverAwareAbstractHandler extends AbstractHandler {

    protected Observer observer;

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public Observer getObserver() {
        return observer;
    }

}
