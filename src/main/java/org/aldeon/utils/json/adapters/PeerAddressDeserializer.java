package org.aldeon.utils.json.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.aldeon.net.Ipv4PeerAddress;
import org.aldeon.net.PeerAddress;

import java.lang.reflect.Type;

public class PeerAddressDeserializer implements JsonDeserializer<PeerAddress> {

    @Override
    public PeerAddress deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        String type = json.getAsJsonObject().get("type").getAsString();

        if(type.equals(Ipv4PeerAddress.TYPE)) {
            return context.deserialize(json, Ipv4PeerAddress.class);
        }

        throw new JsonParseException("Unknown address type");
    }
}
