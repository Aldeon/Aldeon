package org.aldeon.db.wrappers;

import org.aldeon.crypt.Key;
import org.aldeon.db.Db;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Identity;
import org.aldeon.model.Message;
import org.aldeon.model.User;

import java.util.Map;
import java.util.Set;

public abstract class AbstractDbWrapper implements Db {

    protected final Db db;

    public AbstractDbWrapper(Db db) {
        this.db = db;
    }

    @Override
    public void insertMessage(Message message, Callback<InsertResult> callback) {
        db.insertMessage(message, callback);
    }

    @Override
    public void deleteMessage(Identifier msgId, Callback<Boolean> callback) {
        db.deleteMessage(msgId, callback);
    }

    @Override
    public void getMessageById(Identifier msgId, Callback<Message> callback) {
        db.getMessageById(msgId, callback);
    }

    @Override
    public void getMessageXorById(Identifier msgId, Callback<Identifier> callback) {
        db.getMessageXorById(msgId, callback);
    }

    @Override
    public void getMessageIdsByXor(Identifier msgXor, Callback<Set<Identifier>> callback) {
        db.getMessageIdsByXor(msgXor, callback);
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
    public void insertUser(User user, Callback<Boolean> callback) {
        db.insertUser(user, callback);
    }

    @Override
    public void deleteUser(Key publicKey, Callback<Boolean> callback) {
        db.deleteUser(publicKey, callback);
    }

    @Override
    public void getUser(Key publicKey, Callback<User> callback) {
        db.getUser(publicKey, callback);
    }

    @Override
    public void getUsers(Callback<Set<User>> callback) {
        db.getUsers(callback);
    }

    @Override
    public void insertIdentity(Identity identity, Callback<Boolean> callback) {
        db.insertIdentity(identity, callback);
    }

    @Override
    public void deleteIdentity(Key publicKey, Callback<Boolean> callback) {
        db.deleteIdentity(publicKey, callback);
    }

    @Override
    public void getIdentity(Key publicKey, Callback<Identity> callback) {
        db.getIdentity(publicKey, callback);
    }

    @Override
    public void getIdentities(Callback<Set<Identity>> callback) {
        db.getIdentities(callback);
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
