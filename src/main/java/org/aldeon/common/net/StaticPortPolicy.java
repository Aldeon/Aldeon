package org.aldeon.common.net;

public class StaticPortPolicy implements PortPolicy {

    private Port internal;
    private Port external;

    public StaticPortPolicy(Port internal, Port external) {
        this.internal = internal;
        this.external = external;
    }

    @Override
    public Port getInternalPort() {
        return internal;
    }

    @Override
    public Port getExternalPort() {
        return external;
    }
}
