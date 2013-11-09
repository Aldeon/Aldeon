package org.aldeon.db;

import org.aldeon.crypt.Signature;
import org.aldeon.crypt.SignatureImpl;
import org.aldeon.model.Identifier;
import org.aldeon.model.IdentifierImpl;
import org.aldeon.model.Message;
import org.aldeon.events.Callback;
import org.aldeon.model.MessageImpl;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.Executor;

public class DbImpl implements Db {

    @Override
    public void getMessageByIdentifier(Identifier identifier, final Callback<Message> callback, Executor executor) {

        if(identifier == null) {
            callback.call(null);
        } else {

            // Return any thing so we some output in the browser

            Identifier id0 = new IdentifierImpl(randomBuffer(Identifier.LENGTH_BYTES), false);
            Identifier id1 = new IdentifierImpl(randomBuffer(Identifier.LENGTH_BYTES), false);
            Identifier id2 = new IdentifierImpl(randomBuffer(Identifier.LENGTH_BYTES), false);
            Signature sig = new SignatureImpl(randomBuffer(Signature.LENGTH_BYTES), false);
            String content = "Example content";

            Message message = new MessageImpl(id0, id1, id2, sig, content);
            callback.call(message);
        }
    }

    @Override
    public void putMessage(Message message) {

    }

    @Override
    public void dropMessage(Identifier identifier) {

    }

    @Override
    public void getMessageIdentifierByXor(Identifier xor, Callback<Identifier> callback, Executor executor) {
        callback.call(null);
    }

    @Override
    public void getMessageXorByIdentifier(Identifier id, Callback<Identifier> callback, Executor executor) {
        callback.call(null);
    }

    @Override
    public void getMessagesByParent(Identifier parent, Callback<Set<Identifier>> callback, Executor executor) {
        if(parent == null) {
            callback.call(null);
        } else {
            Set<Identifier> children = new HashSet<>();

            children.add(new IdentifierImpl(randomBuffer(Identifier.LENGTH_BYTES), false));
            children.add(new IdentifierImpl(randomBuffer(Identifier.LENGTH_BYTES), false));
            children.add(new IdentifierImpl(randomBuffer(Identifier.LENGTH_BYTES), false));
            callback.call(children);
        }
    }

    @Override
    public void getIdsAndXorsByParent(Identifier parent, Callback<Map<Identifier, Identifier>> callback, Executor executor) {

    }

    public static ByteBuffer randomBuffer(int size) {
        byte[] b = new byte[size];
        new Random().nextBytes(b);
        return ByteBuffer.wrap(b);
    }
}
