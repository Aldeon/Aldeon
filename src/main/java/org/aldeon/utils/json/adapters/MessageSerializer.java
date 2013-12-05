package org.aldeon.utils.json.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.aldeon.crypt.Key;
import org.aldeon.model.Signature;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;

import java.lang.reflect.Type;

public class MessageSerializer implements JsonSerializer<Message> {
    @Override
    public JsonElement serialize(Message src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject object = new JsonObject();

        object.add("id",        context.serialize(src.getIdentifier(), Identifier.class));
        object.add("parent",    context.serialize(src.getParentMessageIdentifier(), Identifier.class));
        object.add("author",    context.serialize(src.getAuthorPublicKey(), Key.class));
        object.add("signature", context.serialize(src.getSignature(), Signature.class));
        object.add("content",   context.serialize(src.getContent()));

        return object;
    }
}
