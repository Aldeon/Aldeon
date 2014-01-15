package org.aldeon.gui2.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.aldeon.gui2.Gui2Utils;
import org.aldeon.gui2.various.DeterministicColorGenerator;
import org.aldeon.gui2.various.ToggleEvent;
import org.aldeon.model.Message;

public class MessageCard extends HorizontalColorContainer {

    @FXML protected Label messageContentLabel;
    @FXML protected Label messageIdLabel;
    @FXML protected Label userNameLabel;
    @FXML protected Label userIdLabel;
    @FXML protected Button responseButton;
    @FXML protected Button removeButton;
    @FXML protected Button toggleChildrenButton;
    protected boolean toggle = false;

    private final ObjectProperty<Message> messageProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<ToggleEvent>> onToggleProperty = new SimpleObjectProperty<>();

    public MessageCard() {
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/MessageCard.fxml", this);

        messageProperty().addListener(new ChangeListener<Message>() {
            @Override
            public void changed(ObservableValue<? extends Message> observableValue, Message oldMessage, Message newMessage) {
                userNameLabel.setText("Anonymous");
                userIdLabel.setText(newMessage.getAuthorPublicKey().toString());
                messageIdLabel.setText(newMessage.getIdentifier().toString());
                messageContentLabel.setText(newMessage.getContent());
                setColor(DeterministicColorGenerator.get(newMessage.getAuthorPublicKey().hashCode()));
            }
        });

        toggleChildrenButton.setText("Show");

        toggleChildrenButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggle = !toggle;
                if(toggle) {
                    toggleChildrenButton.setText("Hide");
                } else {
                    toggleChildrenButton.setText("Show");
                }
                EventHandler<ToggleEvent> eventHandler = getOnToggle();
                if(eventHandler != null) {
                    eventHandler.handle(new ToggleEvent(toggle));
                }
            }
        });
    }

    public MessageCard(Message message) {
        this();
        setMessage(message);
    }

    @FXML protected void onLink(ActionEvent event) {
        Gui2Utils.copyToClipboard(messageIdLabel.getText());
    }

    public ObjectProperty<Message> messageProperty() {
        return messageProperty;
    }

    public Message getMessage() {
        return messageProperty().get();
    }

    public void setMessage(Message message) {
        messageProperty().set(message);
    }

    public ObjectProperty<EventHandler<ActionEvent>> onResponseProperty() {
        return responseButton.onActionProperty();
    }

    public EventHandler<ActionEvent> getOnResponse() {
        return onResponseProperty().get();
    }

    public void setOnResponse(EventHandler<ActionEvent> onResponse) {
        onResponseProperty().set(onResponse);
    }

    public ObjectProperty<EventHandler<ActionEvent>> onRemoveProperty() {
        return removeButton.onActionProperty();
    }

    public EventHandler<ActionEvent> getOnRemove() {
        return onRemoveProperty().get();
    }

    public void setOnRemove(EventHandler<ActionEvent> onRemove) {
        onRemoveProperty().set(onRemove);
    }

    public ObjectProperty<EventHandler<ToggleEvent>> onToggleProperty() {
        return onToggleProperty;
    }

    public EventHandler<ToggleEvent> getOnToggle() {
        return onToggleProperty().get();
    }

    public void setOnToggle(EventHandler<ToggleEvent> onToggle) {
        onToggleProperty().set(onToggle);
    }

    public boolean getToggleState() {
        return toggle;
    }
}
