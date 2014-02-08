package org.aldeon.gui.components;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.aldeon.gui.GuiUtils;
import org.aldeon.gui.various.MessageEvent;
import org.aldeon.gui.various.ToggleEvent;
import org.aldeon.model.Message;

public class MessageWithChildren extends BorderPane {

    @FXML protected BorderPane messageContainer;
    @FXML protected BorderPane responseContainer;
    @FXML protected VBox childrenContainer;
    protected MessageCard messageCard;

    protected ObjectProperty<Message> messageProperty = new SimpleObjectProperty<>();
    private final ObservableList<MessageWithChildren> childrenList = FXCollections.observableArrayList();
    private ObjectProperty<EventHandler<MessageEvent>> onNewMessage = new SimpleObjectProperty<>();

    public MessageWithChildren(){
        super();
        GuiUtils.loadFXMLandInjectController("/gui/fxml/components/MessageWithChildren.fxml", this);

        messageCard = new MessageCard();
        messageContainer.setCenter(messageCard);

        messageProperty().addListener(new ChangeListener<Message>() {
            @Override
            public void changed(ObservableValue<? extends Message> observableValue, Message oldMessage, Message newMessage) {
                messageCard.setMessage(newMessage);
            }
        });

        messageCard.setOnResponse(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MessageCreator creator = new MessageCreator(messageCard.getMessage().getIdentifier());
                responseContainer.setPadding(new Insets(16.0, 0, 0, 16.0));
                creator.setOnCreatorClosed(new EventHandler<MessageEvent>() {
                    @Override
                    public void handle(MessageEvent messageEvent) {
                        responseContainer.setPadding(new Insets(0));
                        responseContainer.setCenter(null);
                        if(messageEvent.message() != null) {
                            EventHandler<MessageEvent> handler = getOnNewMessage();
                            if(handler != null) {
                                handler.handle(messageEvent);
                            }
                        }
                    }
                });
                responseContainer.setCenter(creator);
            }
        });

        Bindings.bindContent(childrenContainer.getChildren(), childrenList);
    }

    public ObjectProperty<Message> messageProperty() {
        return messageProperty;
    }

    public Message getMessage() {
        return messageProperty().get();
    }

    public void setMessage(Message value) {
        messageProperty().set(value);
    }

    public ObservableList<MessageWithChildren> childrenContainers() {
        return childrenList;
    }

    public ObjectProperty<EventHandler<ToggleEvent>> toggleProperty() {
        return messageCard.onToggleProperty();
    }

    public EventHandler<ToggleEvent> getToggle() {
        return toggleProperty().get();
    }

    public void setToggle(EventHandler<ToggleEvent> handler) {
        toggleProperty().set(handler);
    }

    public ObjectProperty<EventHandler<ActionEvent>> onRemoveProperty() {
        return messageCard.onRemoveProperty();
    }

    public EventHandler<ActionEvent> getOnRemove() {
        return onRemoveProperty().get();
    }

    public void setOnRemove(EventHandler<ActionEvent> handler) {
        onRemoveProperty().set(handler);
    }

    public boolean getToggleState() {
        return messageCard.getToggleState();
    }

    public ObjectProperty<EventHandler<MessageEvent>> onNewMessageProperty() {
        return onNewMessage;
    }

    public void setOnNewMessage(EventHandler<MessageEvent> handler) {
        onNewMessageProperty().set(handler);
    }

    public EventHandler<MessageEvent> getOnNewMessage() {
        return onNewMessageProperty().get();
    }

}
