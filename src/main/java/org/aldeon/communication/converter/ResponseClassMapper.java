package org.aldeon.communication.converter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.CompareTreesRequest;
import org.aldeon.protocol.request.GetClockRequest;
import org.aldeon.protocol.request.GetMessageRequest;
import org.aldeon.protocol.request.GetRelevantPeersRequest;
import org.aldeon.protocol.request.IndicateInterestRequest;
import org.aldeon.protocol.response.AddressDiscardedResponse;
import org.aldeon.protocol.response.AddressSavedResponse;
import org.aldeon.protocol.response.BranchInSyncResponse;
import org.aldeon.protocol.response.ChildrenResponse;
import org.aldeon.protocol.response.ClockResponse;
import org.aldeon.protocol.response.DiffResponse;
import org.aldeon.protocol.response.LuckyGuessResponse;
import org.aldeon.protocol.response.MessageFoundResponse;
import org.aldeon.protocol.response.MessageNotFoundResponse;
import org.aldeon.protocol.response.RelevantPeersResponse;
import org.aldeon.utils.json.ClassMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Class mapper used to decode a JSON string into a Response object.
 */
public class ResponseClassMapper implements ClassMapper<Response> {

    public static final String TYPE_FIELD = "type";

    private static final Map<String, Class<? extends Response>> classMap;

    static {
        classMap = new HashMap<>();

        classMap.put(AddressDiscardedResponse.TYPE,     AddressDiscardedResponse.class);
        classMap.put(AddressSavedResponse.TYPE,         AddressSavedResponse.class);
        classMap.put(BranchInSyncResponse.TYPE,         BranchInSyncResponse.class);
        classMap.put(ChildrenResponse.TYPE,             ChildrenResponse.class);
        classMap.put(ClockResponse.TYPE,                ClockResponse.class);
        classMap.put(DiffResponse.TYPE,                 DiffResponse.class);
        classMap.put(LuckyGuessResponse.TYPE,           LuckyGuessResponse.class);
        classMap.put(MessageFoundResponse.TYPE,         MessageFoundResponse.class);
        classMap.put(MessageNotFoundResponse.TYPE,      MessageNotFoundResponse.class);
        classMap.put(RelevantPeersResponse.TYPE,        RelevantPeersResponse.class);
    }

    @Override
    public Class<? extends Response> getClass(Object object) {

        /*

            This should be implemented before we start writing
            the client part of the application.

         */

        if(object instanceof JsonObject) {
            JsonObject jsonObject = (JsonObject) object;
            JsonElement typeElement = jsonObject.get(TYPE_FIELD);
            if(typeElement != null) {
                String action = typeElement.getAsString();
                return classMap.get(action);
            }
        }

        return null;
    }
}
