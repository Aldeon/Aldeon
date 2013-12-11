package org.aldeon.utils.json.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.aldeon.networking.NetworkingModule;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.exceptions.AddressParseException;

import java.lang.reflect.Type;

public class PeerAddressDeserializer implements JsonDeserializer<PeerAddress> {

    @Override
    public PeerAddress deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        String type = json.getAsJsonObject().get("type").getAsString();
        String address = json.getAsJsonObject().get("address").getAsString();

        PeerAddress result;
        try {
            result = NetworkingModule.deserialize(new AddressType(type), address);
        } catch (AddressParseException e) {
            throw new JsonParseException("Failed to deserialize a given address", e);
        }

        if(result == null) {
            throw new JsonParseException("Null is not a valid address");
        } else {
            return result;
        }
    }
}
