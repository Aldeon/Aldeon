package org.aldeon.db.wrappers;

import org.aldeon.db.Db;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;
import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.codec.hex.HexCodec;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class DbLoggerDecorator extends AbstractDbWrapper {

    private final static Codec hex = new HexCodec();
    private final static Random r = new Random();

    public DbLoggerDecorator(Db db) {
        super(db);
    }

    private static String randomHex() {
        byte[] bytes = new byte[4];
        r.nextBytes(bytes);
        return hex.encode(ByteBuffer.wrap(bytes));
    }

    @Override
    public void getMessageById(final Identifier msgId, final Callback<Message> callback) {
        System.out.println("------------");
        final String rand = randomHex();

        System.out.println(rand + " QUERY: select message where id=" + msgId);
        db.getMessageById(msgId, new Callback<Message>() {
            @Override
            public void call(Message val) {
                System.out.println(rand + " RESULT: ");
                System.out.println(rand + " " + val);
                callback.call(val);
            }
        });
    }

    @Override
    public void insertMessage(Message message, Callback<InsertResult> callback) {
        System.out.println("------------");
        final String rand = randomHex();
        System.out.println(rand + " QUERY: insert message");
        System.out.println(rand + " " + message);
        db.insertMessage(message, callback);
        System.out.println(rand + " RESULT: inserted");
    }

    @Override
    public void deleteMessage(Identifier msgId, Callback<Boolean> callback) {
        System.out.println("------------");
        final String rand = randomHex();
        System.out.println(rand + " QUERY: delete message " + msgId);
        db.deleteMessage(msgId, callback);
        System.out.println(rand + " RESULT: inserted");
    }

    @Override
    public void getMessageXorById(Identifier msgId, final Callback<Identifier> callback) {
        System.out.println("------------");
        final String rand = randomHex();

        System.out.println(rand + " QUERY: select xor where id=" + msgId);
        db.getMessageXorById(msgId, new Callback<Identifier>() {
            @Override
            public void call(Identifier val) {
                System.out.println(rand + " RESULT: " + val);
                callback.call(val);
            }
        });
    }

    @Override
    public void getMessageIdsByXor(Identifier msgXor, final Callback<Set<Identifier>> callback) {
        System.out.println("------------");
        final String rand = randomHex();

        System.out.println(rand + " QUERY: select id where xor=" + msgXor);
        db.getMessageIdsByXor(msgXor, new Callback<Set<Identifier>>() {
            @Override
            public void call(Set<Identifier> val) {
                System.out.println(rand + " BEGIN RESULTS: ");
                for(Identifier id: val) {
                    System.out.println(rand + " ID: " + id);
                }
                System.out.println(rand + " END RESULTS");
                callback.call(val);
            }
        });
    }

    @Override
    public void getMessagesByParentId(Identifier parentId, final Callback<Set<Message>> callback) {
        System.out.println("------------");
        final String rand = randomHex();

        System.out.println(rand + " QUERY: select message where parent = " + parentId);
        db.getMessagesByParentId(parentId, new Callback<Set<Message>>() {
            @Override
            public void call(Set<Message> val) {
                System.out.println(rand + " BEGIN RESULTS: ");
                for(Message msg: val) {
                    System.out.println(rand + " MSG: " + msg);
                }
                System.out.println(rand + " END RESULTS");
                callback.call(val);
            }
        });
    }

    @Override
    public void getMessageIdsByParentId(Identifier parentId, final Callback<Set<Identifier>> callback) {
        System.out.println("------------");
        final String rand = randomHex();

        System.out.println(rand + " QUERY: select id where parent=" + parentId);
        db.getMessageIdsByParentId(parentId, new Callback<Set<Identifier>>() {
            @Override
            public void call(Set<Identifier> val) {
                System.out.println(rand + " BEGIN RESULTS: ");
                for (Identifier id : val) {
                    System.out.println(rand + " ID: " + id);
                }
                System.out.println(rand + " END RESULTS");
                callback.call(val);
            }
        });
    }

    @Override
    public void getIdsAndXorsByParentId(Identifier parentId, final Callback<Map<Identifier, Identifier>> callback) {
        System.out.println("------------");
        final String rand = randomHex();

        System.out.println(rand + " QUERY: select id,xor where parent=" + parentId);
        db.getIdsAndXorsByParentId(parentId, new Callback<Map<Identifier, Identifier>>() {
            @Override
            public void call(Map<Identifier, Identifier> val) {
                System.out.println(rand + " BEGIN RESULTS: ");
                for (Map.Entry<Identifier, Identifier> entry : val.entrySet()) {
                    System.out.println(rand + " ID: " + entry.getKey() + " => XOR: " + entry.getValue());
                }
                System.out.println(rand + " END RESULTS");
                callback.call(val);
            }
        });
    }

    @Override
    public void checkAncestry(Identifier descendant, Identifier ancestor, final Callback<Boolean> callback) {
        System.out.println("------------");
        final String rand = randomHex();

        System.out.println(rand + " QUERY: check if " + descendant + " descends from " + ancestor);
        db.checkAncestry(descendant, ancestor, new Callback<Boolean>() {
            @Override
            public void call(Boolean val) {
                System.out.println(rand + " RESULT: " + val);
                callback.call(val);
            }
        });
    }

    @Override
    public void getClock(final Callback<Long> callback) {
        System.out.println("------------");
        final String rand = randomHex();

        System.out.println(rand + "QUERY: select clock");
        db.getClock(new Callback<Long>() {
            @Override
            public void call(Long val) {
                System.out.println(rand + " RESULT: " + val);
                callback.call(val);
            }
        });
    }

    @Override
    public void getMessagesAfterClock(Identifier topic, long clock, final Callback<Set<Message>> callback) {
        System.out.println("------------");
        final String rand = randomHex();

        System.out.println(rand + " QUERY: select message where topic = " + topic + " AND clock > " + clock);
        db.getMessagesAfterClock(topic, clock, new Callback<Set<Message>>() {
            @Override
            public void call(Set<Message> val) {
                System.out.println(rand + " BEGIN RESULTS: ");
                for (Message msg : val) {
                    System.out.println(rand + " MSG: " + msg);
                }
                System.out.println(rand + " END RESULTS");
                callback.call(val);
            }
        });
    }

    @Override
    public void start() {
        System.out.println("------------");
        System.out.println("DB START");
        db.start();
    }

    @Override
    public void close() {
        System.out.println("------------");
        System.out.println("DB CLOSE");
        db.close();
    }
}
