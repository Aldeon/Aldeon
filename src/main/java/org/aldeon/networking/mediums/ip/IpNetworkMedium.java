package org.aldeon.networking.mediums.ip;

import com.google.common.collect.Sets;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.NetworkMedium;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.RecvPoint;
import org.aldeon.networking.common.SendPoint;
import org.aldeon.networking.exceptions.AddressParseException;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;
import org.aldeon.networking.mediums.ip.addresses.IpV4PeerAddress;
import org.aldeon.networking.mediums.ip.nat.upnp.UpnpAddressTranslationFactory;
import org.aldeon.networking.mediums.ip.nat.utils.AddressTranslation;
import org.aldeon.networking.mediums.ip.receiver.NettyRecvPoint;
import org.aldeon.networking.mediums.ip.sender.NettySendPoint;
import org.aldeon.utils.collections.IterableEnumeration;
import org.aldeon.utils.helpers.InetAddresses;
import org.aldeon.utils.net.PortImpl;
import org.aldeon.utils.various.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class IpNetworkMedium implements NetworkMedium {

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private static final Logger log = LoggerFactory.getLogger(IpNetworkMedium.class);
    private static final int PORT = 41530;

    private final RecvPoint recvPoint;
    private final SendPoint sendPoint;

    private static class Iface {
        IpPeerAddress address;
        int mask;
    }

    private List<Iface> interfaces = new LinkedList<>();
    private AddressTranslation natPortMapping = null;
    private IpPeerAddress translatedAddress = null;

    public IpNetworkMedium() {

        // TODO: use ipv6-compatible bind address (http://stackoverflow.com/a/11110685)
        IpPeerAddress loopback = IpPeerAddress.create(new InetSocketAddress(PORT));

        recvPoint = new NettyRecvPoint(loopback);
        sendPoint = new NettySendPoint();

    }

    @Override
    public Set<AddressType> addressTypes() {
        return Sets.newHashSet(IpPeerAddress.IPV4, IpPeerAddress.IPV6);
    }

    @Override
    public SendPoint sendPoint() {
        return sendPoint;
    }

    @Override
    public RecvPoint recvPoint() {
        return recvPoint;
    }

    @Override
    public String serialize(PeerAddress address) throws AddressParseException {
        if(address instanceof IpPeerAddress) {
            return "{\"host\":\"" + ((IpPeerAddress) address).getHost().getHostAddress() + "\",\"port\":" + ((IpPeerAddress) address).getPort().getIntValue() + "}";
        } else {
            throw new AddressParseException("Address is not an instance of IpPeerAddress");
        }
    }

    @Override
    public PeerAddress deserialize(String address) {
        throw new IllegalStateException("Not yet implemented");
    }

    private Iterable<NetworkInterface> networkInterfaces() {
        try {
            return new IterableEnumeration<>(NetworkInterface.getNetworkInterfaces());
        } catch (SocketException e) {
            log.error("Failed to iterate through network interfaces", e);
            return Collections.emptyList();
        }
    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////

    @Override
    public IpPeerAddress machineAddressForForeignAddress(PeerAddress peerAddress) {

        if(!(peerAddress instanceof IpPeerAddress)) {
            return null;
        }

        IpPeerAddress peer = (IpPeerAddress) peerAddress;

        /*
            We need to determine which one of our machine addresses
            should be given to a peer.

            Strategy:
                1. Look for an address in the same subnet.
                2. Look for nat-translated address
                3. Look for any address of the same type
                4. null

         */

        for(Iface iface: interfaces) {
            if(InetAddresses.sameSubnet(peer.getHost(), iface.address.getHost(), iface.mask)) {
                return iface.address;
            }
        }

        if(peer.getType().equals(IpV4PeerAddress.IPV4) && translatedAddress != null) {
            return translatedAddress;
        }

        for(Iface iface: interfaces) {
            if(iface.address.getType().equals(peer.getType())) {
                return iface.address;
            }
        }

        return null;
    }

    @Override
    public void start() {

        boolean hasIpv4 = false;
        boolean onlyPrivateIpv4 = true;

        for(NetworkInterface networkInterface: networkInterfaces()) {
            try {
                if(networkInterface.isLoopback() || networkInterface.isPointToPoint()) {
                    // ignore interface
                } else {
                    for(InterfaceAddress interfaceAddress: networkInterface.getInterfaceAddresses()) {

                        Iface iface = new Iface();
                        iface.address = IpPeerAddress.create(interfaceAddress.getAddress(), PORT);
                        iface.mask = interfaceAddress.getNetworkPrefixLength();
                        interfaces.add(iface);

                        log.info("Detected address: " + iface.address);

                        if(iface.address.getType().equals(IpV4PeerAddress.IPV4)) {
                            hasIpv4 = true;
                            if(!InetAddresses.isLocal(iface.address.getHost())) {
                                onlyPrivateIpv4 = false;
                            }
                        }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        if(hasIpv4 && onlyPrivateIpv4) {
            log.info("No public IPv4 detected. Trying UPnP...");

            int publicPort = 40000 + new Random().nextInt(10000);


            Future<AddressTranslation> future = UpnpAddressTranslationFactory.create(new PortImpl(PORT), new PortImpl(publicPort));

            try {
                natPortMapping = future.get(1000, TimeUnit.MILLISECONDS);
                log.info("Successfully obtained an address translation ("
                        + natPortMapping.getInternalAddress().getHostAddress() + ":" + natPortMapping.getInternalPort() + " -> "
                        + natPortMapping.getExternalAddress().getHostAddress() + ":" + natPortMapping.getExternalPort() + ")");

                translatedAddress = IpPeerAddress.create(natPortMapping.getExternalAddress(), natPortMapping.getExternalPort().getIntValue());

            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                log.info("Failed to obtain an address translation.");
                future.cancel(true);
                natPortMapping = null;
            }

        }
    }

    @Override
    public void close() {
        if(natPortMapping != null) {
            log.info("Shutting down address translation...");
            natPortMapping.shutdown();
        }
    }
}
