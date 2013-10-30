package org.aldeon.db;

import org.aldeon.common.model.Identifier;
import org.aldeon.common.model.Message;
import org.aldeon.common.model.Storage;
import org.aldeon.common.events.Callback;

import java.util.Collection;

public class DbStub implements Storage {

    @Override
    public void getMessageByIdentifier(Identifier identifier, Callback<Message> callback) {

    }

    @Override
    public void putMessage(Message message) {

    }

    @Override
    public void dropMessage(Identifier identifier) {

    }

    @Override
    public void getMessageIdentifierByXor(Identifier xor, Callback<Identifier> callback) {

    }

    @Override
    public void getMessagesByParent(Identifier parent, Callback<Collection<Identifier>> callback) {

    }
}
