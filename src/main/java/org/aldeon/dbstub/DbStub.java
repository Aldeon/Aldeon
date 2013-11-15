package org.aldeon.dbstub;

import org.aldeon.db.Db;
import org.aldeon.db.exception.IdentifierAlreadyPresentException;
import org.aldeon.db.exception.UnknownIdentifierException;
import org.aldeon.db.exception.UnknownParentException;
import org.aldeon.events.AsyncCallback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

public class DbStub implements Db {


    protected static final int PUT_SUCCESS = 1;
    protected static final int PUT_NO_PARENT = 2;
    protected static final int PUT_MSG_EXISTS = 3;

    protected static final int DEL_SUCCESS = 1;
    protected static final int DEL_ID_UNKNOWN = 2;

    protected Map<Identifier, Message> messages;
    protected XorManager mgr;

    protected DbStub(XorManager mgr) {
        messages = new HashMap<>();
        this.mgr = mgr;
    }

    protected int put(Message message) {
        try {
            mgr.putId(message.getIdentifier(), message.getParentMessageIdentifier());
        } catch (UnknownParentException e) {
            return PUT_NO_PARENT;
        } catch (IdentifierAlreadyPresentException e) {
            return PUT_MSG_EXISTS;
        }
        messages.put(message.getIdentifier(), message);
        return PUT_SUCCESS;
    }

    protected int del(Identifier id) {
        try {
            mgr.delId(id);
            messages.remove(id);
        } catch (UnknownIdentifierException e) {
            return DEL_ID_UNKNOWN;
        }
        return DEL_SUCCESS;
    }

    @Override
    public void getMessageById(Identifier msgId, AsyncCallback<Message> callback) {
        callback.call(messages.get(msgId));
    }

    @Override
    public void insertMessage(Message message, Executor executor) {
        put(message);
    }

    @Override
    public void deleteMessage(Identifier msgId, Executor executor) {
        del(msgId);
    }

    @Override
    public void getMessageXorById(Identifier msgId, AsyncCallback<Identifier> callback) {
        try {
            callback.call(mgr.getXor(msgId));
        } catch (UnknownIdentifierException e) {
            callback.call(null);
        }
    }

    @Override
    public void getMessageIdsByXor(Identifier msgXor, AsyncCallback<Set<Identifier>> callback) {
        callback.call(mgr.getIds(msgXor));
    }

    @Override
    public void getMessagesByParentId(Identifier parentId, AsyncCallback<Set<Identifier>> callback) {
        callback.call(mgr.getChildren(parentId));
    }

    @Override
    public void getIdsAndXorsByParentId(Identifier parentId, AsyncCallback<Map<Identifier, Identifier>> callback) {

        Map<Identifier, Identifier> result = new HashMap<>();

        for(Identifier child: mgr.getChildren(parentId)) {
            try {
                result.put(child, mgr.getXor(child));
            } catch (UnknownIdentifierException e) { }
        }

        callback.call(result);
    }
}
