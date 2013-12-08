package org.aldeon.nat;

import org.aldeon.nat.utils.StaticAddressTranslation;
import org.aldeon.networking.common.Port;

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

        /*

            2 interfaces
                eth0:
                    192.168.1.5 /8
                    fe80::1
                eth1:
                    10.0.0.4 /16

            Gościu pisze z 192.168.1.7, mam połaczenie z siecią (więc on raczej też). Powinien otrzymać wszystko co wiem.
            Gościu pisze z 8.8.8.8. Nie powinien otrzymać moich adresów lokalnych


            musimy zagospodarować listę adresów maszyny (nie tylko ip) tak aby każdy adres miał dokładnie jednego sendera
            jeden sender może obsługiwać jednocześnie wiele adresów maszyny
            adres maszyny jest funkcją osoby której tą informację mamy przekazać
            każdy adres może podlegać translacji (rozróżnieniu na adres rzeczywisty i adres widziany przez innych)
            informacje dostępne w dht zależą od adresu osoby pytającej oraz od polityki dostępu do adresów
            istnieje tyle instancji dht ile jest typów adresów
            typ adresu jest niepodzielny - nie ma dalszych podtypów
            każdy adres może być jednocześnie maksymalnie w jednym dht
            Miner niezależny od dht, do niego zgłaszamy się po sloty. Kopie tylko na zapotrzebowanie, jedno zapytanie per poszukiwany typ




         */


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
