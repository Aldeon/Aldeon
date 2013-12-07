package org.aldeon.nat;

import org.aldeon.nat.utils.StaticAddressTranslation;
import org.aldeon.net.AddressTranslation;
import org.aldeon.net.Port;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Addressing {

    private Addressing() { }

    public static AddressTranslation staticTranslation(InetAddress internalHost, Port internalPort, InetAddress externalHost, Port externalPort) {
        return new StaticAddressTranslation(internalHost, internalPort, externalHost, externalPort);
    }

    public static AddressTranslation address(InetAddress host, Port port) {
        return new StaticAddressTranslation(host, port, host, port);
    }

    public static AddressTranslation local() throws UnknownHostException, SocketException {

        Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();

        while(ifaces.hasMoreElements()) {

            NetworkInterface iface = ifaces.nextElement();

            if(! iface.isLoopback()) {
                System.out.println("IFACE: " + iface);
                for(InterfaceAddress addr: iface.getInterfaceAddresses()){
                    System.out.println("IFADDR: " + addr);
                }
            }
        }

        return null;
    }
}
