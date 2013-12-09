package org.aldeon.utils.json.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.aldeon.networking.NetworkingModule;
import org.aldeon.networking.common.PeerAddress;

import java.lang.reflect.Type;

public class PeerAddressSerializer implements JsonSerializer<PeerAddress> {

    @Override
    public JsonElement serialize(PeerAddress address, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject object = new JsonObject();

        object.add("type",      context.serialize(address.getType().getName()));
        object.add("address",   context.serialize(NetworkingModule.serialize(address)));

        return object;
    }
}
