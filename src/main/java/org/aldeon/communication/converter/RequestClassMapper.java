package org.aldeon.communication.converter;

import com.google.gson.JsonObject;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.request.GetMessageRequest;
import org.aldeon.utils.json.ClassMapper;

/**
 * Class mapper used to decode JSON string into a Request.
 */
public class RequestClassMapper implements ClassMapper<Request> {
    @Override
    public Class<? extends Request> getClass(JsonObject jsonObject) {
        String action = jsonObject.get("action").getAsString();

        if(action.equals(GetMessageRequest.TYPE)) {
            return GetMessageRequest.class;
        } else{
            return null;
        }
    }
}
