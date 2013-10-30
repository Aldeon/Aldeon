package org.aldeon.db;

import org.aldeon.crypt.Signature;
import org.aldeon.crypt.SignatureImpl;
import org.aldeon.model.Identifier;
import org.aldeon.model.IdentifierImpl;
import org.aldeon.model.Message;
import org.aldeon.events.Callback;
import org.aldeon.model.MessageImpl;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.Executor;

public class DbStub implements Storage {

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
    public void getMessagesByParent(Identifier parent, Callback<Collection<Identifier>> callback, Executor executor) {
        callback.call(Collections.EMPTY_SET);
    }

    public static ByteBuffer randomBuffer(int size) {
        byte[] b = new byte[size];
        new Random().nextBytes(b);
        return ByteBuffer.wrap(b);
    }
}
