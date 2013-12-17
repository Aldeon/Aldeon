package org.aldeon.networking.mediums.ip;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.NetworkMedium;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.RecvPoint;
import org.aldeon.networking.common.SendPoint;
import org.aldeon.networking.exceptions.AddressParseException;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;
import org.aldeon.networking.mediums.ip.receiver.NettyRecvPoint;
import org.aldeon.networking.mediums.ip.sender.NettySendPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class IpNetworkMedium implements NetworkMedium {

    private static final Logger log = LoggerFactory.getLogger(IpNetworkMedium.class);
    private static final int PORT = 41530;

    private final RecvPoint recvPoint;
    private final SendPoint sendPoint;
    private final SetMultimap<AddressType, IpPeerAddress> machineAddresses = HashMultimap.create();

    public IpNetworkMedium() {

        // TODO: use ipv6-compatible bind address (http://stackoverflow.com/a/11110685)
        IpPeerAddress loopback = IpPeerAddress.create(new InetSocketAddress(PORT));

        recvPoint = new NettyRecvPoint(loopback);
        sendPoint = new NettySendPoint();

        try {
            enumerateAddresses();
        } catch (SocketException e) {
            log.error("Failed to iterate through network interfaces", e);
        }

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
    public Set<PeerAddress> getMachineAddresses(AddressType addressType) {
        return new HashSet<PeerAddress>(machineAddresses.get(addressType));
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

    private void registerMachineAddress(IpPeerAddress address) {
        if(machineAddresses.containsKey(address.getType())) {
            log.info("Multiple addresses for " + address.getType() + " detected - ignoring!");
        }
        machineAddresses.put(address.getType(), address);
    }

    private void enumerateAddresses() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while(networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            if(! networkInterface.isLoopback()) {
                for(InterfaceAddress interfaceAddress: networkInterface.getInterfaceAddresses()){
                    registerMachineAddress(IpPeerAddress.create(interfaceAddress.getAddress(), PORT));
                }
            }
        }
    }
}
