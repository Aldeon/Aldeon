package org.aldeon.utils.helpers;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InetAddressesTest {

    @Test
    public void shouldReturnTrueForMatchingIpv4Addresses() throws UnknownHostException {
        assertTrue(InetAddresses.sameSubnet(
                InetAddress.getByName("192.168.1.1"),
                InetAddress.getByName("192.168.1.50"),
                24));
    }

    @Test
    public void shouldReturnFalseForNotMatchingIpv4Addresses() throws UnknownHostException {
        assertFalse(InetAddresses.sameSubnet(
                InetAddress.getByName("192.168.2.1"),
                InetAddress.getByName("192.168.1.18"),
                24));
    }

    @Test
    public void shouldWorkOnNonOctalMaskAndMatchingIpv4Pair() throws UnknownHostException {
        assertTrue(InetAddresses.sameSubnet(
                InetAddress.getByName("192.168.1.255"),
                InetAddress.getByName("192.168.1.254"),
                31));
    }

    @Test
    public void shouldWorkOnNonOctalMaskAndNonMatchingIpv4Pair() throws UnknownHostException {
        assertFalse(InetAddresses.sameSubnet(
                InetAddress.getByName("192.168.1.15"),
                InetAddress.getByName("192.168.1.129"),
                25));
    }

    @Test
    public void shouldReturnTrueForMatchingIpv6Addresses() throws UnknownHostException {
        assertTrue(InetAddresses.sameSubnet(
                InetAddress.getByName("2001:cdba:0000:0000:0000:0000:3257:9652"),
                InetAddress.getByName("2001:cdba:0000:0000:0000:0000:aaaa:bbbb"),
                64));
    }

    @Test
    public void shouldReturnFalseForNotMatchingIpv6Addresses() throws UnknownHostException {
        assertFalse(InetAddresses.sameSubnet(
                InetAddress.getByName("2001:cdba:0000:0000:0000:0000:3257:9652"),
                InetAddress.getByName("2001:cdba:1100:0000:0000:0000:3257:9652"),
                64));
    }

    @Test
    public void shouldWorkOnNonOctalMaskAndMatchingIpv6Pair() throws UnknownHostException {
        assertTrue(InetAddresses.sameSubnet(
                InetAddress.getByName("2001:cdba:0000:0000:0000:0000:3257:9653"),
                InetAddress.getByName("2001:cdba:0000:0000:0000:0000:3257:9652"),
                125));
    }

    @Test
    public void shouldWorkOnNonOctalMaskAndNonMatchingIpv6Pair() throws UnknownHostException {
        assertFalse(InetAddresses.sameSubnet(
                InetAddress.getByName("2001:cdba:0000:0000:0000:0000:3257:a652"),
                InetAddress.getByName("2001:cdba:0000:0000:0000:0000:3257:9652"),
                125));
    }
}
