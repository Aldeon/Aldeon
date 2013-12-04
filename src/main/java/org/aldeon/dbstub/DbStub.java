package org.aldeon.dbstub;

import org.aldeon.db.Db;
import org.aldeon.db.exception.IdentifierAlreadyPresentException;
import org.aldeon.db.exception.UnknownIdentifierException;
import org.aldeon.db.exception.UnknownParentException;
import org.aldeon.events.AsyncCallback;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/**
 * Imitates the real database. Thread safe, not thread efficient.
 *
 * All tasks should be performed in the callback.getExecutor()'s, but
 * we can ignore it here. After all, this is just for debugging purposes.
 */
public class DbStub implements Db {

    private static final Logger log = LoggerFactory.getLogger(DbStub.class);

    protected static enum PutStatus {
        SUCCESS,
        NO_PARENT,
        MSG_EXISTS
    }

    protected static enum DelStatus {
        SUCCESS,
        ID_UNKNOWN
    }

    protected Map<Identifier, Message> messages;
    protected XorManager mgr;

    protected DbStub(XorManager mgr) {
        messages = new ConcurrentHashMap<>();
        this.mgr = mgr;
    }

    protected PutStatus put(Message message) {
        try {
            mgr.putId(message.getIdentifier(), message.getParentMessageIdentifier());
        } catch (UnknownParentException e) {
            return PutStatus.NO_PARENT;
        } catch (IdentifierAlreadyPresentException e) {
            return PutStatus.MSG_EXISTS;
        }
        messages.put(message.getIdentifier(), message);
        return PutStatus.SUCCESS;
    }

    protected DelStatus del(Identifier id) {
        try {
            mgr.delId(id);
            messages.remove(id);
        } catch (UnknownIdentifierException e) {
            return DelStatus.ID_UNKNOWN;
        }
        return DelStatus.SUCCESS;
    }

    @Override
    public void getMessageById(Identifier msgId, AsyncCallback<Message> callback) {
        callback.call(messages.get(msgId));
    }

    @Override
    public void insertMessage(Message message, Executor executor) {
        PutStatus s = put(message);
    }

    @Override
    public void deleteMessage(Identifier msgId, Executor executor) {
        DelStatus s = del(msgId);
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
    public void getMessagesByParentId(Identifier parentId, AsyncCallback<Set<Message>> callback) {
        Set<Message> result = new HashSet<>();

        for(Identifier child: mgr.getChildren(parentId)) {
            Message msg = messages.get(child);
            if(msg != null) {
                result.add(msg);
            }
        }

        callback.call(result);
    }

    @Override
    public void getMessageIdsByParentId(Identifier parentId, AsyncCallback<Set<Identifier>> callback) {
        callback.call(mgr.getChildren(parentId));
    }

    @Override
    public void getIdsAndXorsByParentId(Identifier parentId, AsyncCallback<Map<Identifier, Identifier>> callback) {

        Map<Identifier, Identifier> result = new HashMap<>();

        for(Identifier child: mgr.getChildren(parentId)) {
            try {
                result.put(child, mgr.getXor(child));
            } catch (UnknownIdentifierException e) {
                // State changed between queries
            }
        }

        callback.call(result);
    }

    @Override
    public void checkAncestry(Identifier descendant, Identifier ancestor, AsyncCallback<Boolean> callback) {
        Identifier current = descendant;
        while(current != null && !current.isEmpty()) {
            Message m = messages.get(current);
            if(m == null) {
                break;
            } else {
                current = m.getParentMessageIdentifier();
                if(current.equals(ancestor)) {
                    callback.call(true);
                    return;
                }
            }
        }
        callback.call(false);
    }

    @Override
    public void getClock(AsyncCallback<Long> callback) {
        // TODO: implement
        callback.call(0l);
    }

    @Override
    public void getMessagesAfterClock(Identifier topic, long clock, Callback<Set<Message>> callback) {

        // TODO: implement
        callback.call(Collections.EMPTY_SET);
    }
}
