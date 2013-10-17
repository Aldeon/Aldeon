package org.aldeon.nat;

import org.aldeon.common.net.PortPolicy;

public abstract class UpnpPortForwardingPolicy implements PortPolicy {

    public static UpnpPortForwardingPolicy create() {

        /*
        Sample implementations:
            - http://4thline.org/projects/cling/support/manual/cling-support-manual.html
            - http://4thline.org/projects/cling/support/xref-test/example/igd/PortMappingTest.html
         */

        return null;
    }
}
