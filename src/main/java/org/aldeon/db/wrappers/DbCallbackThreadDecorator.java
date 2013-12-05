package org.aldeon.db.wrappers;

import org.aldeon.db.Db;
import org.aldeon.events.Callback;
import org.aldeon.events.CallbackAndExecutor;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

public class DbCallbackThreadDecorator implements Db {

    private final Db db;
    private final Executor executor;

    public DbCallbackThreadDecorator(Db db, Executor executor) {
        this.db = db;
        this.executor = executor;
    }

    @Override
    public void getMessageById(Identifier msgId, Callback<Message> callback) {
        db.getMessageById(msgId, new CallbackAndExecutor<>(callback, executor));
    }

    @Override
    public void insertMessage(Message message) {
        db.insertMessage(message);
    }

    @Override
    public void deleteMessage(Identifier msgId) {
        db.deleteMessage(msgId);
    }

    @Override
    public void getMessageXorById(Identifier msgId, Callback<Identifier> callback) {
        db.getMessageXorById(msgId, new CallbackAndExecutor<>(callback, executor));
    }

    @Override
    public void getMessageIdsByXor(Identifier msgXor, Callback<Set<Identifier>> callback) {
        db.getMessageIdsByXor(msgXor, new CallbackAndExecutor<>(callback, executor));
    }

    @Override
    public void getMessagesByParentId(Identifier parentId, Callback<Set<Message>> callback) {
        db.getMessagesByParentId(parentId, new CallbackAndExecutor<>(callback, executor));
    }

    @Override
    public void getMessageIdsByParentId(Identifier parentId, Callback<Set<Identifier>> callback) {
        db.getMessageIdsByParentId(parentId, new CallbackAndExecutor<>(callback, executor));
    }

    @Override
    public void getIdsAndXorsByParentId(Identifier parentId, Callback<Map<Identifier, Identifier>> callback) {
        db.getIdsAndXorsByParentId(parentId, new CallbackAndExecutor<>(callback, executor));
    }

    @Override
    public void checkAncestry(Identifier descendant, Identifier ancestor, Callback<Boolean> callback) {
        db.checkAncestry(descendant, ancestor, new CallbackAndExecutor<>(callback, executor));
    }

    @Override
    public void getClock(Callback<Long> callback) {
        db.getClock(new CallbackAndExecutor<>(callback, executor));
    }

    @Override
    public void getMessagesAfterClock(Identifier topic, long clock, Callback<Set<Message>> callback) {
        db.getMessagesAfterClock(topic, clock, new CallbackAndExecutor<>(callback, executor));
    }
}
