package org.aldeon.db;

import org.aldeon.common.model.Identifier;
import org.aldeon.common.model.Message;
import org.aldeon.common.model.Storage;
import org.aldeon.utils.various.Callback;

import java.util.Collection;

public class DbStub implements Storage {

    @Override
    public void getMessageByIdentifier(Identifier identifier, Callback<Message> callback) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void putMessage(Message message) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void dropMessage(Identifier identifier) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void getMessageIdentifierByXor(Identifier xor, Callback<Identifier> callback) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void getMessagesByParent(Identifier parent, Callback<Collection<Identifier>> callback) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
