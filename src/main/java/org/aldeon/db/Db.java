package org.aldeon.db;

import org.aldeon.events.AsyncCallback;
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
    void getMessageIdByXor(Identifier msgXor, AsyncCallback<Identifier> callback);
    void getMessagesByParentId(Identifier parentId, AsyncCallback<Set<Identifier>> callback);
    void getIdsAndXorsByParentId(Identifier parentId, AsyncCallback<Map<Identifier, Identifier>> callback);
}
