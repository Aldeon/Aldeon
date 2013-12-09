package org.aldeon.db.wrappers;

import org.aldeon.core.CoreModule;
import org.aldeon.core.events.MessageAddedEvent;
import org.aldeon.core.events.MessageRemovedEvent;
import org.aldeon.db.Db;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;

import java.util.Map;
import java.util.Set;


public class DbEventCallerDecorator implements Db {

    private final Db db;

    public DbEventCallerDecorator(Db db){
        this.db = db;
    }

    @Override
    public void getMessageById(Identifier msgId, Callback<Message> callback) {
        db.getMessageById(msgId,callback);
    }

    @Override
    public void insertMessage(Message message) {
        CoreModule.getInstance().getEventLoop().notify(new MessageAddedEvent(message));
        db.insertMessage(message);
    }

    @Override
    public void deleteMessage(Identifier msgId) {
        CoreModule.getInstance().getEventLoop().notify(new MessageRemovedEvent(msgId));
        db.deleteMessage(msgId);
    }

    @Override
    public void getMessageXorById(Identifier msgId, Callback<Identifier> callback) {
        db.getMessageXorById(msgId,callback);
    }

    @Override
    public void getMessageIdsByXor(Identifier msgXor, Callback<Set<Identifier>> callback) {
        db.getMessageIdsByXor(msgXor,callback);
    }

    @Override
    public void getMessagesByParentId(Identifier parentId, Callback<Set<Message>> callback) {
        db.getMessagesByParentId(parentId, callback);
    }

    @Override
    public void getMessageIdsByParentId(Identifier parentId, Callback<Set<Identifier>> callback) {
        db.getMessageIdsByParentId(parentId, callback);
    }

    @Override
    public void getIdsAndXorsByParentId(Identifier parentId, Callback<Map<Identifier, Identifier>> callback) {
        db.getIdsAndXorsByParentId(parentId, callback);
    }

    @Override
    public void checkAncestry(Identifier descendant, Identifier ancestor, Callback<Boolean> callback) {
        db.checkAncestry(descendant, ancestor, callback);
    }

    @Override
    public void getClock(Callback<Long> callback) {
        db.getClock(callback);
    }

    @Override
    public void getMessagesAfterClock(Identifier topic, long clock, Callback<Set<Message>> callback) {
        db.getMessagesAfterClock(topic, clock, callback);
    }

    @Override
    public void start() {
        db.start();
    }

    @Override
    public void close() {
        db.close();
    }
}
