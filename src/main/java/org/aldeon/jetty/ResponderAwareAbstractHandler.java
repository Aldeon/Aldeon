package org.aldeon.jetty;

import org.aldeon.common.Responder;
import org.eclipse.jetty.server.handler.AbstractHandler;

abstract class ResponderAwareAbstractHandler extends AbstractHandler {

    protected Responder responder;

    public void setResponder(Responder responder) {
        this.responder = responder;
    }

    public Responder getResponder() {
        return responder;
    }

}
