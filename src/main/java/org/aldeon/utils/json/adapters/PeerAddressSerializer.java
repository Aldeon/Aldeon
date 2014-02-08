package org.aldeon.utils.json.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.aldeon.networking.NetworkingModule;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.exceptions.AddressParseException;

import java.lang.reflect.Type;

public class PeerAddressSerializer implements JsonSerializer<PeerAddress> {

    @Override
    public JsonElement serialize(PeerAddress address, Type typeOfSrc, JsonSerializationContext context) {

        String addressSerialized;
        try {
            addressSerialized = NetworkingModule.serialize(address);
        } catch (AddressParseException e) {
            throw new JsonParseException("Failed to serialize the given address", e);
        }

        if(addressSerialized == null) {
            throw new JsonParseException("Null is not a valid result of serialization");
        }

        JsonObject object = new JsonObject();
        JsonParser parser = new JsonParser();

        object.add("type", context.serialize(address.getType().getName()));
        object.add("data", parser.parse(addressSerialized));

        return object;
    }
}
