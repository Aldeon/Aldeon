package org.aldeon.utils.json.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.aldeon.networking.NetworkingModule;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.PeerAddress;

import java.lang.reflect.Type;

public class PeerAddressDeserializer implements JsonDeserializer<PeerAddress> {

    @Override
    public PeerAddress deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        String type = json.getAsJsonObject().get("type").getAsString();
        String address = json.getAsJsonObject().get("address").getAsString();

        PeerAddress result = NetworkingModule.deserialize(new AddressType(type), address);

        if(result == null) {
            throw new JsonParseException("Failed to deserialize an address");
        } else {
            return result;
        }
    }
}
