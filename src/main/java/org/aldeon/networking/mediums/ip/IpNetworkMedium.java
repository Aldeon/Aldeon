package org.aldeon.networking.mediums.ip;

import com.google.common.collect.Sets;
import org.aldeon.config.Config;
import org.aldeon.core.CoreModule;
import org.aldeon.core.services.ServiceManager;
import org.aldeon.events.Callback;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.NetworkMedium;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.RecvPoint;
import org.aldeon.networking.common.SendPoint;
import org.aldeon.networking.exceptions.AddressParseException;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;
import org.aldeon.networking.mediums.ip.addresses.IpV4PeerAddress;
import org.aldeon.networking.mediums.ip.discovery.LocalPeerDiscoveryService;
import org.aldeon.networking.mediums.ip.nat.upnp.UpnpAddressTranslationFactory;
import org.aldeon.networking.mediums.ip.nat.utils.AddressTranslation;
import org.aldeon.networking.mediums.ip.receiver.NettyRecvPoint;
import org.aldeon.networking.mediums.ip.sender.NettySendPoint;
import org.aldeon.utils.collections.IterableEnumeration;
import org.aldeon.utils.helpers.InetAddresses;
import org.aldeon.utils.json.JsonModule;
import org.aldeon.utils.json.JsonParser;
import org.aldeon.utils.net.PortImpl;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Aggregates all functionality related to TCP/IP communication.
 */
public class IpNetworkMedium implements NetworkMedium {

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private static final Logger log = LoggerFactory.getLogger(IpNetworkMedium.class);
    private static final JsonParser parser = new JsonModule().get();
    private static final int FALLBACK_PORT = 41530;

    private final RecvPoint recvPoint;
    private final SendPoint sendPoint;
    private Integer definedPort = null;
    private ServiceManager serviceManager;

    private static class Iface {
        IpPeerAddress address;
        int mask;
    }

    private List<Iface> interfaces = new LinkedList<>();
    private AddressTranslation natPortMapping = null;
    private IpPeerAddress translatedAddress = null;

    public IpNetworkMedium() {

        // TODO: use ipv6-compatible bind address (http://stackoverflow.com/a/11110685)
        IpPeerAddress loopback = IpPeerAddress.create(new InetSocketAddress(port()));

        recvPoint = new NettyRecvPoint(loopback);
        sendPoint = new NettySendPoint();
        serviceManager = new ServiceManager();
    }

    private int port() {
        if(definedPort == null) {

            // Read port from configuration
            Configuration cfg = Config.config();

            if(cfg.getBoolean("port.randomize")) {
                definedPort = new Random().nextInt(50000) + 10000;
            } else {
                definedPort = cfg.getInt("port.value");
            }

        }

        if(definedPort <= 0 || definedPort > 65535) {
            log.warn("Invalid port value (" + definedPort + ") using " + FALLBACK_PORT + " instead.");
            definedPort = FALLBACK_PORT;
        }

        return definedPort;
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
    public IpPeerAddress deserialize(String address) throws AddressParseException {
        try {
            IpPeerAddressTemplate template = parser.fromJson(address, IpPeerAddressTemplate.class);
            InetAddress host = com.google.common.net.InetAddresses.forString(template.host);
            return IpPeerAddress.create(host, template.port);
        } catch (Exception e) {
            throw new AddressParseException("Failed to deserialize the given string to IpPeerAddress", e);
        }
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
    public IpPeerAddress localAddressForRemoteAddress(PeerAddress peerAddress) {

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
    public Set<IpPeerAddress> localAddresses() {
        Set<IpPeerAddress> addresses = new HashSet<>();
        for(Iface iface: interfaces) {
            addresses.add(iface.address);
        }
        if(translatedAddress != null) {
            addresses.add(translatedAddress);
        }
        return addresses;
    }

    @Override
    public boolean remoteAddressBelievable(PeerAddress address) {

        /*
            Address is considered believable if:
                - it is not a local address with a different port
                - it is not in an unreachable private subnet //TODO: figure out how to check if network is unreachable
                - it is not a loopback address (0.0.0.0, 127.0.0.1 itp)
         */

        if(! (address instanceof IpPeerAddress)) return false;
        IpPeerAddress peer = (IpPeerAddress) address;

        if(peer.getHost().isAnyLocalAddress() || peer.getHost().isLoopbackAddress()) {
            return false;
        }

        for(IpPeerAddress localAddress: localAddresses()) {
            if(localAddress.getHost().equals(peer.getHost())) {
                return false;
            }
        }

        return true;
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
                        iface.address = IpPeerAddress.create(interfaceAddress.getAddress(), port());
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

            int upnpPublicPort = 40000 + new Random().nextInt(10000);

            Future<AddressTranslation> future = UpnpAddressTranslationFactory.create(new PortImpl(port()), new PortImpl(upnpPublicPort));

            try {
                natPortMapping = future.get(6000, TimeUnit.MILLISECONDS);
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

        if(Config.config().getBoolean("peers.local-discovery.enabled")) {
            LocalPeerDiscoveryService discoveryService = new LocalPeerDiscoveryService(new Callback<IpPeerAddress>() {
                @Override
                public void call(IpPeerAddress address) {
                    log.info("Local peer detected: " + address);
                    if(CoreModule.isInitialized()) {
                        CoreModule.getInstance().getDht().closenessTracker().addAddress(address);
                    }
                }
            });
            for(Iface iface: interfaces) {
                discoveryService.advertiseAddress(iface.address, iface.mask);
            }
            serviceManager.registerService(discoveryService);
        }
        serviceManager.start();
    }

    @Override
    public void close() {
        serviceManager.close();
        if(natPortMapping != null) {
            log.info("Shutting down address translation...");
            natPortMapping.shutdown();
        }
    }

    public static class IpPeerAddressTemplate {
        public String host;
        public int port;
    }
}
