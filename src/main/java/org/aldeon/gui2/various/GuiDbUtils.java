package org.aldeon.gui2.various;

import org.aldeon.core.CoreModule;
import org.aldeon.db.Db;
import org.aldeon.db.wrappers.DbCallbackThreadDecorator;
import org.aldeon.db.wrappers.DbWorkThreadDecorator;
import org.aldeon.events.Callback;
import org.aldeon.model.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GuiDbUtils {

    private static Db db;

    static {
        db = CoreModule.getInstance().getStorage();
        db = new DbCallbackThreadDecorator(db, CoreModule.getInstance().clientSideExecutor());
        db = new DbWorkThreadDecorator(db, CoreModule.getInstance().clientSideExecutor());
    }

    public static Db db() {
        return db;
    }

    public static void collectAncestors(Message message, final Callback<List<Message>> onFinished) {
        recursivelyCollectAncestors(message, new Callback<LinkedList<Message>>() {
            @Override
            public void call(LinkedList<Message> val) {
                onFinished.call(val);
            }
        });
    }

    public static void collectChildren(Message message, final Callback<List<Message>> onFinished) {
        db().getMessagesByParentId(message.getIdentifier(), new Callback<Set<Message>>() {
            @Override
            public void call(Set<Message> val) {
                onFinished.call(new LinkedList<>(val));
            }
        });
    }

    private static void recursivelyCollectAncestors(Message message, final Callback<LinkedList<Message>> onFinished) {
        if(message.getParentMessageIdentifier().isEmpty()) {
            onFinished.call(new LinkedList<Message>());
        } else {
            db().getMessageById(message.getParentMessageIdentifier(), new Callback<Message>() {
                @Override
                public void call(final Message ancestor) {
                    if(ancestor == null) {
                        onFinished.call(new LinkedList<Message>());
                    } else {
                        recursivelyCollectAncestors(ancestor, new Callback<LinkedList<Message>>() {
                            @Override
                            public void call(LinkedList<Message> ancestors) {
                                ancestors.addLast(ancestor);
                                onFinished.call(ancestors);
                            }
                        });
                    }
                }
            });
        }
    }
}
