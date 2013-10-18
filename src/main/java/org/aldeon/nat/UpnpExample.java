package org.aldeon.nat;

import org.aldeon.common.net.ConnectionPolicy;
import org.aldeon.common.net.Port;
import org.aldeon.common.net.PortImpl;
import org.aldeon.nat.upnp.UpnpPolicyFactory;

import java.io.IOException;
import java.net.InetAddress;

public class UpnpExample {
    public static void main(String[] args) throws IOException, InterruptedException {

        Port internalPort = new PortImpl(80);
        Port externalPort = new PortImpl(12346);
        InetAddress ip = InetAddress.getByName("192.168.1.90");

        ConnectionPolicyFactory factory = UpnpPolicyFactory.create(internalPort, externalPort, ip);

        System.out.println("Trying to map a port...");

        factory.begin();

        int cycles = 0;

        while(!factory.isReady()) {
            Thread.sleep(100);
            if(++cycles > 5) break;
        }

        ConnectionPolicy policy = factory.getPolicy();

        if(policy == null) {
            System.out.println("No policy obtained.");
            factory.abort();
            return;
        }

        System.out.println("Success!");

        System.out.println("Internal port : " + policy.getInternalPort());
        System.out.println("External port : " + policy.getExternalPort());
        System.out.println("Internal IP   : " + policy.getInternalAddress());
        System.out.println("External IP   : " + policy.getExternalAddress());

        System.in.read();

        policy.close();
    }
}
