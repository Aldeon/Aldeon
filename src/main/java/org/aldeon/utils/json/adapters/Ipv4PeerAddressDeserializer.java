package org.aldeon.utils.json.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.aldeon.net.Ipv4PeerAddress;
import org.aldeon.net.Port;
import org.aldeon.utils.net.PortImpl;

import java.lang.reflect.Type;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Ipv4PeerAddressDeserializer implements JsonDeserializer<Ipv4PeerAddress> {

    @Override
    public Ipv4PeerAddress deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Inet4Address addr;
        Port port;

        try {
            addr = (Inet4Address) InetAddress.getByName(json.getAsJsonObject().get("host").getAsString());
            port = new PortImpl(json.getAsJsonObject().get("port").getAsInt());
        } catch (UnknownHostException e) {
            throw new JsonParseException("Could not parse the given Ipv4 address", e);
        }

        return new Ipv4PeerAddress(addr, port);
    }

}
