package org.aldeon.utils.helpers;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

public class InetAddresses {

    private static final int INET4_BYTES = 4;
    private static final int INET6_BYTES = 16;

    public static boolean sameSubnet(InetAddress a, InetAddress b, int mask) {
        if(a instanceof Inet4Address && b instanceof Inet4Address) {
            return sameSubnet((Inet4Address) a, (Inet4Address) b, mask);
        }

        if(a instanceof Inet6Address && b instanceof Inet6Address) {
            return sameSubnet((Inet6Address) a, (Inet6Address) b, mask);
        }

        return false;
    }

    private static boolean sameSubnet(Inet4Address a, Inet4Address b, int mask) {
        int m = ~(0xffffffff >>> mask);
        return (intValue(a) & m) == (intValue(b) & m);
    }

    private static boolean sameSubnet(Inet6Address a, Inet6Address b, int mask) {
        byte[] x = a.getAddress();
        byte[] y = b.getAddress();

        for(int i = 0; i < mask / 8; ++i) {
            if(x[i] != y[i]) return false;
        }

        if(mask % 8 > 0 && mask * 8 != INET6_BYTES) {
            byte m = (byte) ~(0xFF >> (mask % 8));
            return (x[mask / 8] & m) == (y[mask / 8] & m);
        }
        return true;
    }

    public static int intValue(Inet4Address address) {
        byte[] b = address.getAddress();
        int val = 0;
        for(int i = 0; i < INET4_BYTES; ++i) {
            val += b[i] << (INET4_BYTES - 1 - i) * 8;
        }
        return val;
    }

    public static boolean isLocal(InetAddress address) {
        return  sameSubnet(address, com.google.common.net.InetAddresses.forString("10.0.0.0"), 8)
            ||  sameSubnet(address, com.google.common.net.InetAddresses.forString("172.16.0.0"), 12)
            ||  sameSubnet(address, com.google.common.net.InetAddresses.forString("192.168.0.0"), 16);
    }
}
