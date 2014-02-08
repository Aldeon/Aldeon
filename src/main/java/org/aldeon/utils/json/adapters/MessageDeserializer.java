package org.aldeon.utils.json.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.aldeon.crypt.Key;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;
import org.aldeon.model.Signature;
import org.aldeon.model.exception.CorruptedMessageException;
import org.aldeon.utils.helpers.Messages;

import java.lang.reflect.Type;

public class MessageDeserializer implements JsonDeserializer<Message> {
    @Override
    public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();

        Identifier id       = context.deserialize(obj.get("id"), Identifier.class);
        Identifier parent   = context.deserialize(obj.get("parent"), Identifier.class);
        Key author          = context.deserialize(obj.get("author"), Key.class);
        String content      = context.deserialize(obj.get("content"), String.class);
        Signature signature = context.deserialize(obj.get("signature"), Signature.class);

        try {
            return Messages.create(id, parent, author, content, signature);
        } catch (CorruptedMessageException e) {
            throw new JsonParseException("Could not parse the message", e);
        }
    }
}
