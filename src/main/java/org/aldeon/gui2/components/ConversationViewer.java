package org.aldeon.gui2.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.aldeon.events.Callback;
import org.aldeon.gui2.Gui2Utils;
import org.aldeon.gui2.various.FxCallback;
import org.aldeon.gui2.various.GuiDbUtils;
import org.aldeon.model.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ConversationViewer extends BorderPane {

    @FXML protected VBox parents;
    @FXML protected VBox children;
    @FXML protected VBox focused;
    @FXML protected Button back;

    private final ObjectProperty<Message> focus = new SimpleObjectProperty<>();

    public ConversationViewer() {
        super();
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/ConversationViewer.fxml", this);

        focusProperty().addListener(new ChangeListener<Message>() {
            @Override
            public void changed(ObservableValue<? extends Message> observableValue, Message oldMessage, Message newMessage) {
                updateViewer(newMessage);
            }
        });
    }

    private void updateViewer(Message message) {
        parents.getChildren().clear();
        children.getChildren().clear();
        focused.getChildren().clear();

        GuiDbUtils.collectAncestors(message, new FxCallback<List<Message>>() {
            @Override
            protected void react(List<Message> messageList) {
                for(Message ancestor: messageList) {
                    addParent(ancestor);
                }
            }
        });

        GuiDbUtils.collectChildren(message, new FxCallback<List<Message>>() {
            @Override
            protected void react(List<Message> val) {
                for(Message child: val) {
                    addChild(child);
                }
            }
        });

        addToFocus(message);
    }

    private void addParent(Message message) {
        parents.getChildren().add(createCard(message));
    }

    private void addChild(Message message) {
        children.getChildren().add(createCard(message));
    }

    private void addToFocus(Message message) {
        focused.getChildren().add(createCard(message));
    }

    private MessageCard createCard(final Message message) {
        MessageCard card = new MessageCard(message);
        card.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setFocus(message);
            }
        });
        return card;
    }

    /* -- getters/setters -- */

    public ObjectProperty<Message> focusProperty() {
        return focus;
    }

    public Message getFocus() {
        return focusProperty().get();
    }

    public void setFocus(Message message) {
        focusProperty().set(message);
    }

    public ObjectProperty<EventHandler<? super MouseEvent>> onViewerClosedProperty() {
        return back.onMouseClickedProperty();
    }

    public EventHandler<? super MouseEvent> getOnViewerClosed() {
        return onViewerClosedProperty().get();
    }

    public void setOnViewerClosed(EventHandler<? super MouseEvent> eventHandler) {
        onViewerClosedProperty().set(eventHandler);
    }

}
