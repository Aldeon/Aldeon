package org.aldeon.jetty.handler;

import org.aldeon.common.Observer;
import org.eclipse.jetty.server.handler.AbstractHandler;

public abstract class ObserverAwareAbstractHandler extends AbstractHandler {

    protected Observer observer;

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public Observer getObserver() {
        return observer;
    }

}
