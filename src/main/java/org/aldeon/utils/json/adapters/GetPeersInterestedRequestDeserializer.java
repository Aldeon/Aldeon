package org.aldeon.utils.json.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.aldeon.model.Identifier;
import org.aldeon.net.Ipv4PeerAddress;
import org.aldeon.net.Ipv6PeerAddress;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.request.GetRelevantPeersRequest;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GetPeersInterestedRequestDeserializer implements JsonDeserializer<GetRelevantPeersRequest> {

    private final Map<String, Class<? extends PeerAddress>> map;

    public GetPeersInterestedRequestDeserializer() {
        map = new HashMap<>();
        map.put(Ipv4PeerAddress.TYPE, Ipv4PeerAddress.class);
        map.put(Ipv6PeerAddress.TYPE, Ipv6PeerAddress.class);
    }

    @Override
    public GetRelevantPeersRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        GetRelevantPeersRequest req = new GetRelevantPeersRequest();

        req.target = context.deserialize(json.getAsJsonObject().get("target"), Identifier.class);

        for(JsonElement type: json.getAsJsonObject().get("types").getAsJsonArray()) {
            Class<? extends PeerAddress> addressType = map.get(type.getAsString());
            if(addressType != null) {
                req.acceptedTypes.add(addressType);
            }
        }
        return req;
    }
}
