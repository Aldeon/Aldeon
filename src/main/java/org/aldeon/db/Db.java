package org.aldeon.db;

import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;
import org.aldeon.model.Service;

import java.util.Map;
import java.util.Set;

public interface Db extends Service {
    void getMessageById(Identifier msgId, Callback<Message> callback);
    void insertMessage(Message message);
    void deleteMessage(Identifier msgId);

    void getMessageXorById(Identifier msgId, Callback<Identifier> callback);
    void getMessageIdsByXor(Identifier msgXor, Callback<Set<Identifier>> callback);
    void getMessagesByParentId(Identifier parentId, Callback<Set<Message>> callback);
    void getMessageIdsByParentId(Identifier parentId, Callback<Set<Identifier>> callback);
    void getIdsAndXorsByParentId(Identifier parentId, Callback<Map<Identifier, Identifier>> callback);
    void checkAncestry(Identifier descendant, Identifier ancestor, Callback<Boolean> callback);
    void getClock(Callback<Long> callback);
    void getMessagesAfterClock(Identifier topic, long clock, Callback<Set<Message>> callback);
}
