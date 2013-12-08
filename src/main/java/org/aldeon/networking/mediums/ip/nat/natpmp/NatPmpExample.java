package org.aldeon.networking.mediums.ip.nat.natpmp;

/*
 * This file is part of jNAT-PMPlib.
 *
 * jNAT-PMPlib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jNAT-PMPlib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jNAT-PMPlib.  If not, see <http://www.gnu.org/licenses/>.
 */

import org.aldeon.networking.mediums.ip.nat.natpmp.jnatpmplib.ExternalAddressRequestMessage;
import org.aldeon.networking.mediums.ip.nat.natpmp.jnatpmplib.MapRequestMessage;
import org.aldeon.networking.mediums.ip.nat.natpmp.jnatpmplib.NatPmpDevice;
import org.aldeon.networking.mediums.ip.nat.natpmp.jnatpmplib.NatPmpException;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.IOException;
import java.net.Inet4Address;

/**
 * This class provides an example that describes how this library is used.
 * @author flszen
 */
public class NatPmpExample {

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    /**
     * This method demonstrates how to use the library to work with a NAT-PMP
     * gateway.
     */
    public static void main(String[] args) {

        try {
            // To find the device, simply construct the class. An exception is
            // thrown if the device cannot be located or if the network is not
            // RFC1918.
            // When the device is constructed, you have to tell it whether you
            // want it to automatically shutdown with the JVM or if you'll take
            // the responsibility of shutting it down yourself. Refer to the
            // constructor documentation for the details. In this case, we'll
            // let it shut down with the JVM.

            NatPmpDevice pmpDevice = new NatPmpDevice(false);

            // The next step is always to determine the external address of
            // the device. This is done by constructing the request message
            // and enqueueing it.
            ExternalAddressRequestMessage extAddr = new ExternalAddressRequestMessage(null);
            pmpDevice.enqueueMessage(extAddr);

            // In this example, we want to purposefully wait until the queue is
            // empty. It is possible to receive notification when the operation
            // is complete. Refer to the documentation for the
            // ExternalAddressRequestMessage constructor.
            pmpDevice.waitUntilQueueEmpty();

            // We can try and get the external address to determine if the
            // gateway is functional.
            // This may throw an exception if there was an error receiving the
            // response. The method getResponseException() would also return an
            // exception object in this case, if you prefer avoiding using
            // try/catch for logic.
            Inet4Address extIP = extAddr.getExternalAddress();

            // Now, we can set up a port mapping. Refer to the javadoc for
            // the parameter values. This message sets up a TCP redirect from
            // a gateway-selected available external port to the local port
            // 80. The lifetime is 120 seconds. In implementation, you would
            // want to consider having a longer lifetime and periodicly sending
            // a MapRequestMessage to prevent it from expiring.
            MapRequestMessage map = new MapRequestMessage(true, 80, 0, 120, null);
            pmpDevice.enqueueMessage(map);
            pmpDevice.waitUntilQueueEmpty();

            // Let's find out what the external port is.
            Integer extPort = map.getExternalPort();

            if(extIP == null || extPort == null) {
                System.out.println("Looks like something went wrong.");
            } else {
                System.out.println("Port 80 should now be visible on " + extIP + ":" + extPort);
                System.in.read();
            }

            pmpDevice.shutdown();

            // Please refer to the javadoc if you run into trouble. As always,
            // contact a developer on the SourceForge project or post in the
            // forums if you have questions.
        } catch (NatPmpException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
