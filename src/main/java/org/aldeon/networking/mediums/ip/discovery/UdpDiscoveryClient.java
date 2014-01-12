package org.aldeon.networking.mediums.ip.discovery;

import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class UdpDiscoveryClient implements Runnable {

    private final DatagramPacket packet;

    public UdpDiscoveryClient(IpPeerAddress address, InetAddress broadcast, int broadcastPort) {

        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(address.getPort().getIntValue());

        byte[] data = new byte[2];
        data[0] = buffer.get(2);
        data[1] = buffer.get(3);

        packet = new DatagramPacket(data, data.length, broadcast, broadcastPort);
    }

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();
        } catch (IOException e) {
            System.out.println("failed");
        }
    }
}
