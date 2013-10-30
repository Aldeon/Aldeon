package org.aldeon.common.model;

import org.aldeon.common.events.Callback;

import java.util.Collection;

public interface Storage {
    void getMessageByIdentifier(Identifier identifier, Callback<Message> callback);
    void putMessage(Message message);
    void dropMessage(Identifier identifier);

    void getMessageIdentifierByXor(Identifier xor, Callback<Identifier> callback);
    void getMessagesByParent(Identifier parent, Callback<Collection<Identifier>> callback);
}
