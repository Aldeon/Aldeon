package org.aldeon.networking.mediums.ip.discovery;

import org.aldeon.events.Callback;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;
import org.aldeon.utils.various.GenericServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class UdpDiscoveryServer extends GenericServer<IpPeerAddress> {

    private static final Logger log = LoggerFactory.getLogger(UdpDiscoveryServer.class);
    private final Callback<IpPeerAddress> onReceived;

    private int port;
    private DatagramSocket socket;
    private byte[] buffer;

    public UdpDiscoveryServer(int port, Callback<IpPeerAddress> onReceived) {
        this.port = port;
        this.buffer = new byte[128];  // 2 bytes would be enough, but meeeeh...
        this.onReceived = onReceived;
    }

    @Override
    protected void handle(IpPeerAddress data) {
        onReceived.call(data);
    }

    @Override
    protected IpPeerAddress provide() {
        DatagramPacket packet;
        try {
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
        } catch (IOException e) {
            // No problem, just return null
            return null;
        }

        // check if the data matches
        if(packet.getLength() == 2) {

            ByteBuffer b = ByteBuffer.allocate(4);
            b.put(2, buffer[0]);
            b.put(3, buffer[1]);
            int port = b.getInt(0);
            InetAddress host = packet.getAddress();
            return IpPeerAddress.create(host, port);
        } else {
            return null;
        }
    }

    @Override
    protected boolean initialize() {
        log.info("Starting UDP local discovery server on port " + port);
        try {
            socket = new DatagramSocket(port);
            return true;
        } catch (SocketException e) {
            log.warn("Failed to start the UDP server on port " + port, e);
            return false;
        }
    }

    @Override
    protected void finalize() {
        log.info("Closing UDP local discovery server...");
        socket.close();
    }
}
