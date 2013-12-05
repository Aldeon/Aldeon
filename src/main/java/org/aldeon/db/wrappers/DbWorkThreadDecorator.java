package org.aldeon.db.wrappers;

import org.aldeon.db.Db;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

public class DbWorkThreadDecorator implements Db {

    private final Db db;
    private final Executor executor;

    public DbWorkThreadDecorator(Db db, Executor executor) {
        this.db = db;
        this.executor = executor;
    }

    @Override
    public void getMessageById(final Identifier msgId, final Callback<Message> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.getMessageById(msgId, callback);
            }
        });
    }

    @Override
    public void insertMessage(final Message message) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.insertMessage(message);
            }
        });
    }

    @Override
    public void deleteMessage(final Identifier msgId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.deleteMessage(msgId);
            }
        });
    }

    @Override
    public void getMessageXorById(final Identifier msgId, final Callback<Identifier> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.getMessageXorById(msgId, callback);
            }
        });
    }

    @Override
    public void getMessageIdsByXor(final Identifier msgXor, final Callback<Set<Identifier>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.getMessageIdsByXor(msgXor, callback);
            }
        });
    }

    @Override
    public void getMessagesByParentId(final Identifier parentId, final Callback<Set<Message>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.getMessagesByParentId(parentId, callback);
            }
        });
    }

    @Override
    public void getMessageIdsByParentId(final Identifier parentId, final Callback<Set<Identifier>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.getMessageIdsByParentId(parentId, callback);
            }
        });
    }

    @Override
    public void getIdsAndXorsByParentId(final Identifier parentId, final Callback<Map<Identifier, Identifier>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.getIdsAndXorsByParentId(parentId, callback);
            }
        });
    }

    @Override
    public void checkAncestry(final Identifier descendant, final Identifier ancestor, final Callback<Boolean> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.checkAncestry(descendant, ancestor, callback);
            }
        });
    }

    @Override
    public void getClock(final Callback<Long> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.getClock(callback);
            }
        });
    }

    @Override
    public void getMessagesAfterClock(final Identifier topic, final long clock, final Callback<Set<Message>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.getMessagesAfterClock(topic, clock, callback);
            }
        });
    }
}
