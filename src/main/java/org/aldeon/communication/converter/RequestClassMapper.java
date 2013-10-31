package org.aldeon.communication.converter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.request.GetMessageRequest;
import org.aldeon.utils.json.ClassMapper;

/**
 * Class mapper used to decode JSON string into a Request.
 */
public class RequestClassMapper implements ClassMapper<Request> {

    public static final String TYPE_FIELD = "type";

    @Override
    public Class<? extends Request> getClass(JsonObject jsonObject) {

        /*
            We assume that each request has a field named 'type'.
            By reading this value, we can figure out what this
            message should be converted to.

         */
        JsonElement typeElement = jsonObject.get(TYPE_FIELD);

        if(typeElement != null) {

            String action = typeElement.getAsString();

            if(action.equals(GetMessageRequest.TYPE)) {
                return GetMessageRequest.class;
            }
            /*
                else if, else if...
             */
        }

        return null;
    }
}
