package org.aldeon.db;

import org.aldeon.events.AsyncCallback;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

public interface Db {
    void getMessageById(Identifier msgId, AsyncCallback<Message> callback);
    void insertMessage(Message message, Executor executor);
    void deleteMessage(Identifier msgId, Executor executor);

    void getMessageXorById(Identifier msgId, AsyncCallback<Identifier> callback);
    void getMessageIdsByXor(Identifier msgXor, AsyncCallback<Set<Identifier>> callback);
    void getMessagesByParentId(Identifier parentId, AsyncCallback<Set<Identifier>> callback);
    void getIdsAndXorsByParentId(Identifier parentId, AsyncCallback<Map<Identifier, Identifier>> callback);
    void checkAncestry(Identifier descendant, Identifier ancestor, AsyncCallback<Boolean> callback);
    void getClock(AsyncCallback<Long> callback);
    void getMessageIdsAfterClock(Identifier topic, long clock, Callback<Set<Identifier>> callback);
}
