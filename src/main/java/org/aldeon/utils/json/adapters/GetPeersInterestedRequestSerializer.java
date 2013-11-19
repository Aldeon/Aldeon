package org.aldeon.utils.json.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.aldeon.model.Identifier;
import org.aldeon.net.Ipv4PeerAddress;
import org.aldeon.net.Ipv6PeerAddress;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.request.GetRelevantPeersRequest;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GetPeersInterestedRequestSerializer implements JsonSerializer<GetRelevantPeersRequest> {

    private final Map<Class, String> typeMap;

    public GetPeersInterestedRequestSerializer() {
        this.typeMap = new HashMap<>();

        typeMap.put(Ipv4PeerAddress.class, Ipv4PeerAddress.TYPE);
        typeMap.put(Ipv6PeerAddress.class, Ipv6PeerAddress.TYPE);
    }

    @Override
    public JsonElement serialize(GetRelevantPeersRequest src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject obj = new JsonObject();

        obj.add("type",         context.serialize(GetRelevantPeersRequest.TYPE));
        obj.add("target",       context.serialize(src.target, Identifier.class));

        Set<String> types = new HashSet<>();

        for(Class<? extends PeerAddress> type: src.acceptedTypes) {
            String v = typeMap.get(type);
            if(v != null) {
                types.add(v);
            }
        }

        obj.add("types",        context.serialize(types));

        return obj;
    }
}
