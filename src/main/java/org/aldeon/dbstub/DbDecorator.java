package org.aldeon.dbstub;

import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.core.events.InboundMessageEvent;
import org.aldeon.core.events.RemoveMessageEvent;
import org.aldeon.db.Db;
import org.aldeon.events.AsyncCallback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * Created with IntelliJ IDEA.
 * User: Prophet
 * Date: 17.11.13
 * Time: 11:41
 * To change this template use File | Settings | File Templates.
 */
public class DbDecorator implements Db {

    private Db db;

    public DbDecorator(Db db){
        this.db=db;
    }

    @Override
    public void getMessageById(Identifier msgId, AsyncCallback<Message> callback) {
        db.getMessageById(msgId,callback);
    }

    @Override
    public void insertMessage(Message message, Executor executor) {
        CoreModule.getInstance().getEventLoop().notify(new InboundMessageEvent(message));
        db.insertMessage(message,executor);
    }

    @Override
    public void deleteMessage(Identifier msgId, Executor executor) {
        CoreModule.getInstance().getEventLoop().notify(new RemoveMessageEvent(msgId));
        db.deleteMessage(msgId,executor);
    }

    @Override
    public void getMessageXorById(Identifier msgId, AsyncCallback<Identifier> callback) {
        db.getMessageXorById(msgId,callback);
    }

    @Override
    public void getMessageIdsByXor(Identifier msgXor, AsyncCallback<Set<Identifier>> callback) {
        db.getMessageIdsByXor(msgXor,callback);
    }

    @Override
    public void getMessagesByParentId(Identifier parentId, AsyncCallback<Set<Identifier>> callback) {
        db.getMessagesByParentId(parentId,callback);
    }

    @Override
    public void getIdsAndXorsByParentId(Identifier parentId, AsyncCallback<Map<Identifier, Identifier>> callback) {
        db.getIdsAndXorsByParentId(parentId,callback);
    }
}
