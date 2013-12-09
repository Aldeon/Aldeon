package org.aldeon.networking.mediums.ip;

import com.google.common.collect.Sets;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.NetworkMedium;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.Port;
import org.aldeon.networking.common.RecvPoint;
import org.aldeon.networking.common.SendPoint;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;
import org.aldeon.networking.mediums.ip.receiver.NettyRecvPoint;
import org.aldeon.networking.mediums.ip.sender.NettySendPoint;
import org.aldeon.utils.net.PortImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IpNetworkMedium implements NetworkMedium {

    private static final Logger log = LoggerFactory.getLogger(IpNetworkMedium.class);
    private static final int PORT = 41530;

    private final RecvPoint recvPoint;
    private final SendPoint sendPoint;
    private final Map<AddressType, IpPeerAddress> machineAddresses = new HashMap<>();

    public IpNetworkMedium() {

        IpPeerAddress loopback = IpPeerAddress.create("0.0.0.0", PORT);

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
    public IpPeerAddress getMachineAddress(AddressType addressType) {
        return machineAddresses.get(addressType);
    }

    @Override
    public String serialize(PeerAddress address) {
        throw new IllegalStateException("Not yet implemented");
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
