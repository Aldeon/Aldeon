package org.aldeon.utils.json.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.aldeon.net.Ipv4PeerAddress;

import java.lang.reflect.Type;

public class Ipv4PeerAddressSerializer implements JsonSerializer<Ipv4PeerAddress> {
    @Override
    public JsonElement serialize(Ipv4PeerAddress address, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject obj = new JsonObject();

        obj.addProperty("type", Ipv4PeerAddress.TYPE);
        obj.addProperty("host", address.getHost().getHostAddress());
        obj.addProperty("port", address.getPort().getIntValue());

        return obj;
    }
}
