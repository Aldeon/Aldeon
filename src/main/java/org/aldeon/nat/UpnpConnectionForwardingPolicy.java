package org.aldeon.nat;

import org.aldeon.common.net.ConnectionPolicy;

public abstract class UpnpConnectionForwardingPolicy implements ConnectionPolicy {

    public static UpnpConnectionForwardingPolicy create() {

        /*
        Sample implementations:
            - http://4thline.org/projects/cling/support/manual/cling-support-manual.html
            - http://4thline.org/projects/cling/support/xref-test/example/igd/PortMappingTest.html
         */

        return null;
    }
}
