package org.aldeon.db;

import org.aldeon.core.services.Service;
import org.aldeon.crypt.Key;
import org.aldeon.events.Callback;
import org.aldeon.model.*;

import java.util.Map;
import java.util.Set;

public interface Db extends Service {
    void insertMessage(Message message, Callback<InsertResult> callback);
    void deleteMessage(Identifier msgId, Callback<Boolean> callback);
    void getMessageById(Identifier msgId, Callback<Message> callback);
    void getMessageXorById(Identifier msgId, Callback<Identifier> callback);
    void getMessageIdsByXor(Identifier msgXor, Callback<Set<Identifier>> callback);
    void getMessagesByParentId(Identifier parentId, Callback<Set<Message>> callback);
    void getMessageIdsByParentId(Identifier parentId, Callback<Set<Identifier>> callback);
    void getIdsAndXorsByParentId(Identifier parentId, Callback<Map<Identifier, Identifier>> callback);
    void checkAncestry(Identifier descendant, Identifier ancestor, Callback<Boolean> callback);
    void getClock(Callback<Long> callback);
    void getMessagesAfterClock(Identifier topic, long clock, Callback<Set<Message>> callback);
    void dumpMessages(Callback<Set<Message>> callback);

    void insertUser(User user, Callback<Boolean> callback);
    void deleteUser(Key publicKey, Callback<Boolean> callback);
    void getUser(Key publicKey, Callback<User> callback);
    void getUsers(Callback<Set<User>> callback);

    void insertIdentity(Identity identity, Callback<Boolean> callback);
    void deleteIdentity(Key publicKey, Callback<Boolean> callback);
    void getIdentity(Key publicKey, Callback<Identity> callback);
    void getIdentities(Callback<Set<Identity>> callback);

    public enum InsertResult {
        INSERTED,
        ALREADY_EXISTS,
        NO_PARENT,
        CRITICAL_ERROR
    }
}
