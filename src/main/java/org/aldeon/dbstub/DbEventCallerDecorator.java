package org.aldeon.dbstub;

import org.aldeon.core.CoreModule;
import org.aldeon.core.events.MessageAddedEvent;
import org.aldeon.core.events.MessageRemovedEvent;
import org.aldeon.db.Db;
import org.aldeon.events.AsyncCallback;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;


public class DbEventCallerDecorator implements Db {

    private final Db db;

    public DbEventCallerDecorator(Db db){
        this.db = db;
    }

    @Override
    public void getMessageById(Identifier msgId, AsyncCallback<Message> callback) {
        db.getMessageById(msgId,callback);
    }

    @Override
    public void insertMessage(Message message, Executor executor) {
        CoreModule.getInstance().getEventLoop().notify(new MessageAddedEvent(message));
        db.insertMessage(message,executor);
    }

    @Override
    public void deleteMessage(Identifier msgId, Executor executor) {
        CoreModule.getInstance().getEventLoop().notify(new MessageRemovedEvent(msgId));
        db.deleteMessage(msgId,executor);
    }

    @Override
    public void getMessageXorById(Identifier msgId, AsyncCallback<Identifier> callback) {
        db.getMessageXorById(msgId,callback);
    }

    @Override
    public void getMessageIdsByXor(Identifier msgXor, AsyncCallback<Set<Identifier>> callback) {
        db.getMessageIdsByXor(msgXor,callback);
    }

    @Override
    public void getMessagesByParentId(Identifier parentId, AsyncCallback<Set<Identifier>> callback) {
        db.getMessagesByParentId(parentId,callback);
    }

    @Override
    public void getIdsAndXorsByParentId(Identifier parentId, AsyncCallback<Map<Identifier, Identifier>> callback) {
        db.getIdsAndXorsByParentId(parentId, callback);
    }

    @Override
    public void checkAncestry(Identifier descendant, Identifier ancestor, AsyncCallback<Boolean> callback) {
        db.checkAncestry(descendant, ancestor, callback);
    }

    @Override
    public void getClock(AsyncCallback<Long> callback) {
        db.getClock(callback);
    }

    @Override
    public void getMessagesAfterClock(Identifier topic, long clock, Callback<Set<Message>> callback) {

    }
}
