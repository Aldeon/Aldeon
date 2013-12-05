package org.aldeon.communication.converter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.request.CompareTreesRequest;
import org.aldeon.protocol.request.GetClockRequest;
import org.aldeon.protocol.request.GetMessageRequest;
import org.aldeon.protocol.request.GetRelevantPeersRequest;
import org.aldeon.protocol.request.IndicateInterestRequest;
import org.aldeon.utils.json.ClassMapper;

/**
 * Class mapper used to decode JSON string into a Request.
 */
public class RequestClassMapper implements ClassMapper<Request> {

    public static final String TYPE_FIELD = "type";

    @Override
    public Class<? extends Request> getClass(Object object) {

        /*
            We assume that each request has a field named 'type'.
            By reading this value, we can figure out what this
            message should be converted to.

         */

        if(object instanceof JsonObject) {

            JsonElement typeElement = ((JsonObject) object).get(TYPE_FIELD);

            if(typeElement != null) {

                String action = typeElement.getAsString();

                if(action.equals(GetMessageRequest.TYPE)) {
                    return GetMessageRequest.class;
                } else if (action.equals(GetRelevantPeersRequest.TYPE)) {
                    return GetRelevantPeersRequest.class;
                } else if (action.equals(CompareTreesRequest.TYPE)) {
                    return CompareTreesRequest.class;
                } else if (action.equals(IndicateInterestRequest.TYPE)) {
                    return IndicateInterestRequest.class;
                } else if (action.equals(GetClockRequest.TYPE)) {
                    return GetClockRequest.class;
                }
            }
        }

        return null;
    }
}
