package org.aldeon.gui2.various;

import org.aldeon.events.Callback;
import org.aldeon.gui2.Gui2Utils;
import org.aldeon.model.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GuiDbUtils {

    public static void collectAncestors(Message message, final Callback<List<Message>> onFinished) {
        recursivelyCollectAncestors(message, new Callback<LinkedList<Message>>() {
            @Override
            public void call(LinkedList<Message> val) {
                onFinished.call(val);
            }
        });
    }

    public static void collectChildren(Message message, final Callback<List<Message>> onFinished) {
        Gui2Utils.db().getMessagesByParentId(message.getIdentifier(), new Callback<Set<Message>>() {
            @Override
            public void call(Set<Message> val) {
                onFinished.call(new LinkedList<Message>(val));
            }
        });
    }

    private static void recursivelyCollectAncestors(Message message, final Callback<LinkedList<Message>> onFinished) {
        if(message.getParentMessageIdentifier().isEmpty()) {
            onFinished.call(new LinkedList<Message>());
        } else {
            Gui2Utils.db().getMessageById(message.getParentMessageIdentifier(), new Callback<Message>() {
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
