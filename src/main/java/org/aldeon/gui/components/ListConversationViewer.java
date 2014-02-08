package org.aldeon.gui.components;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.aldeon.core.events.MessageAddedEvent;
import org.aldeon.db.Db;
import org.aldeon.events.AsyncCallback;
import org.aldeon.gui.Gui2Utils;
import org.aldeon.gui.various.FxCallback;
import org.aldeon.gui.various.GuiDbUtils;
import org.aldeon.gui.various.MessageEvent;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;
import org.aldeon.utils.helpers.Callbacks;

import java.util.Iterator;
import java.util.List;

public class ListConversationViewer extends ConversationViewer {

    @FXML protected VBox ancestors;
    @FXML protected VBox children;
    @FXML protected VBox focused;

    private final ObservableList<MessageCard> ancestorCards = FXCollections.observableArrayList();
    private final ObservableList<MessageCard> childrenCards = FXCollections.observableArrayList();

    private AsyncCallback<MessageAddedEvent> messageAddedCallback;

    public ListConversationViewer() {
        super();
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/ListConversationViewer.fxml", this);
        focusProperty().addListener(new ChangeListener<Message>() {
            @Override
            public void changed(ObservableValue<? extends Message> observableValue, Message oldMessage, Message newMessage) {
                if(newMessage == null) {
                    ancestorCards.clear();
                    childrenCards.clear();
                    focused.getChildren().clear();
                    return;
                }
                if (cardListContains(ancestorCards, newMessage)) {
                    trimAncestors(newMessage);
                } else if (cardListContains(childrenCards, newMessage)) {
                    ancestorCards.add(card(oldMessage));
                } else {
                    loadAncestorsFromDb(newMessage);
                }
                loadChildrenFromDb(newMessage);
                focused.getChildren().clear();
                focused.getChildren().add(card(newMessage));
            }
        });
        Bindings.bindContent(ancestors.getChildren(), ancestorCards);
        Bindings.bindContent(children.getChildren(), childrenCards);

        messageAddedCallback = new FxCallback<MessageAddedEvent>() {
            @Override
            protected void react(MessageAddedEvent event) {
                if(event.getMessage().getParentMessageIdentifier().equals(getFocus().getIdentifier())) {
                    childrenCards.add(card(event.getMessage()));
                }
            }
        };

        Gui2Utils.loop().assign(MessageAddedEvent.class, messageAddedCallback);
    }

    private MessageCard card(final Message message) {
        MessageCard card = new MessageCard(message);
        card.hideToggleChildrenButton();
        card.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setFocus(message);
            }
        });
        card.setOnResponse(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setFocus(message);
                children.getChildren().add(0, creator(message.getIdentifier()));
            }
        });
        card.setOnRemove(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GuiDbUtils.db().deleteMessage(message.getIdentifier(), new FxCallback<Boolean>() {
                    @Override
                    protected void react(Boolean removedSuccessfully) {
                        if(message.getParentMessageIdentifier().isEmpty()) {
                            //TODO: indicate to parent that conversation closed
                            setFocus(null);
                        } else {
                            if(getFocus().getIdentifier().equals(message.getParentMessageIdentifier())) {
                                removeChild(message.getIdentifier());
                            } else {
                                // reload view
                                setFocus(findParentInAncestors(message.getParentMessageIdentifier()));
                            }
                        }
                    }
                });
            }
        });
        return card;
    }

    private MessageCreator creator(Identifier parent) {
        final MessageCreator creator = new MessageCreator(parent);
        creator.setOnCreatorClosed(new EventHandler<MessageEvent>() {
            @Override
            public void handle(final MessageEvent messageEvent) {
                if(messageEvent.message() != null) {
                    GuiDbUtils.db().insertMessage(messageEvent.message(), Callbacks.<Db.InsertResult>emptyCallback());
                }
                children.getChildren().remove(creator);
            }
        });
        return creator;
    }

    private boolean cardListContains(List<MessageCard> list, Message message) {
        for(MessageCard card: list) {
            if(card.getMessage().equals(message)) {
                return true;
            }
        }
        return false;
    }

    /* -- DB access -- */

    private void loadChildrenFromDb(Message message) {
        childrenCards.clear();
        children.getChildren().clear(); // remove writer if it exists
        GuiDbUtils.collectChildren(message, new FxCallback<List<Message>>() {
            @Override
            protected void react(List<Message> childrenList) {
                for(Message child: childrenList) {
                    childrenCards.add(card(child));
                }
            }
        });
    }

    private void loadAncestorsFromDb(Message message) {
        ancestorCards.clear();
        GuiDbUtils.collectAncestors(message, new FxCallback<List<Message>>() {
            @Override
            protected void react(List<Message> ancestorsList) {
                for(Message ancestor: ancestorsList) {
                    ancestorCards.add(card(ancestor));
                }
            }
        });
    }

    private void trimAncestors(Message message) {
        Iterator<MessageCard> it = ancestorCards.iterator();
        boolean found = false;
        while(it.hasNext()) {
            if(found) {
                it.next();
                it.remove();
            } else if(it.next().getMessage().equals(message)) {
                found = true;
                it.remove();
            }
        }
    }

    private Message findParentInAncestors(Identifier parentId) {
        for(MessageCard card: ancestorCards) {
            if(card.getMessage().getIdentifier().equals(parentId)) {
                return card.getMessage();
            }
        }
        return null;
    }

    private void removeChild(Identifier identifier) {
        Iterator<MessageCard> it  = childrenCards.iterator();
        while(it.hasNext()) {
            if(it.next().getMessage().getIdentifier().equals(identifier)) {
                it.remove();
                break;
            }
        }
    }

    @Override
    public void onRemovedFromScene() {
        Gui2Utils.loop().resign(MessageAddedEvent.class, messageAddedCallback);
    }
}
