package org.aldeon.db;

import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

public interface Db {
    void getMessageById(Identifier msgId, Callback<Message> callback, Executor executor);
    void insertMessage(Message message);
    void deleteMessage(Identifier msgId);

    void getMessageXorById(Identifier msgId, Callback<Identifier> callback, Executor executor);
    void getMessageIdByXor(Identifier msgXor, Callback<Identifier> callback, Executor executor);
    void getMessagesByParentId(Identifier parentId, Callback<Set<Identifier>> callback, Executor executor);
    void getIdsAndXorsByParentId(Identifier parentId, Callback<Map<Identifier, Identifier>> callback, Executor executor);
}
