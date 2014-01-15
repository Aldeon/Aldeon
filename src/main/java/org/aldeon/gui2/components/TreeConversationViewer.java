package org.aldeon.gui2.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import org.aldeon.core.events.MessageAddedEvent;
import org.aldeon.core.events.MessageRemovedEvent;
import org.aldeon.db.Db;
import org.aldeon.gui2.Gui2Utils;
import org.aldeon.gui2.various.FxCallback;
import org.aldeon.gui2.various.GuiDbUtils;
import org.aldeon.gui2.various.MessageEvent;
import org.aldeon.gui2.various.ToggleEvent;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;
import org.aldeon.utils.helpers.Callbacks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TreeConversationViewer extends ConversationViewer {

    @FXML protected ScrollPane scrollPane;

    protected Map<Identifier, MessageWithChildren> containersMap = new HashMap<>();

    public TreeConversationViewer(Message topic) {
        super();
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/TreeConversationViewer.fxml", this);
        scrollPane.setContent(createContainer(topic));

        Gui2Utils.loop().assign(MessageAddedEvent.class, new FxCallback<MessageAddedEvent>() {
            @Override
            protected void react(MessageAddedEvent val) {
                MessageWithChildren container = containersMap.get(val.getMessage().getParentMessageIdentifier());
                if(container != null) {
                    if(container.getToggleState()) {
                        container.childrenContainers().add(createContainer(val.getMessage()));
                    }
                }
            }
        });

        Gui2Utils.loop().assign(MessageRemovedEvent.class, new FxCallback<MessageRemovedEvent>() {
            @Override
            protected void react(MessageRemovedEvent val) {
                MessageWithChildren container = containersMap.get(val.getIdentifier());
                if(container != null) {
                    Identifier parent = container.getMessage().getParentMessageIdentifier();
                    if(parent.isEmpty()) {
                        setFocus(null);
                    } else {
                        clearAndRemoveContainer(container);
                        containersMap.remove(val.getIdentifier());
                        containersMap.get(parent).childrenContainers().remove(container);
                    }
                }
            }
        });
    }

    protected MessageWithChildren createContainer(Message message) {
        final MessageWithChildren messageWithChildren = new MessageWithChildren();
        messageWithChildren.setMessage(message);

        messageWithChildren.setToggle(new EventHandler<ToggleEvent>() {
            @Override
            public void handle(ToggleEvent toggleEvent) {

                if(toggleEvent.getToggle()) {
                    loadChildren(messageWithChildren);
                } else {
                    clearAndRemoveContainer(messageWithChildren);
                }
            }
        });

        messageWithChildren.setOnRemove(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GuiDbUtils.db().deleteMessage(messageWithChildren.getMessage().getIdentifier(), Callbacks.<Boolean>emptyCallback());
            }
        });

        messageWithChildren.setOnNewMessage(new EventHandler<MessageEvent>() {
            @Override
            public void handle(MessageEvent messageEvent) {
                GuiDbUtils.db().insertMessage(messageEvent.message(), Callbacks.<Db.InsertResult>emptyCallback());
            }
        });

        containersMap.put(message.getIdentifier(), messageWithChildren);
        return messageWithChildren;
    }

    private void clearAndRemoveContainer(MessageWithChildren container) {
        Iterator<MessageWithChildren> it = container.childrenContainers().iterator();
        while(it.hasNext()) {
            MessageWithChildren tmp = it.next();
            containersMap.remove(tmp.getMessage().getIdentifier());
            clearAndRemoveContainer(tmp);
            it.remove();
        }
    }

    protected void loadChildren(final MessageWithChildren container) {
        GuiDbUtils.db().getMessagesByParentId(container.getMessage().getIdentifier(), new FxCallback<Set<Message>>() {
            @Override
            protected void react(Set<Message> val) {
                for(Message child: val) {
                    container.childrenContainers().add(createContainer(child));
                }
            }
        });
    }

    @Override
    public void onRemovedFromScene() {

    }
}
