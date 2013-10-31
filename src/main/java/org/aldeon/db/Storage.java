package org.aldeon.db;

import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;

import java.util.Collection;
import java.util.concurrent.Executor;

public interface Storage {
    void getMessageByIdentifier(Identifier identifier, Callback<Message> callback, Executor executor);
    void putMessage(Message message);
    void dropMessage(Identifier identifier);

    void getMessageIdentifierByXor(Identifier xor, Callback<Identifier> callback, Executor executor);
    void getMessagesByParent(Identifier parent, Callback<Collection<Identifier>> callback, Executor executor);
}
