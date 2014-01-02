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
import org.aldeon.model.Message;

public class TopicCard extends HorizontalColorContainer {

    private static final String PLAY_ICON = "/gui2/images/message-icons/play.png";
    private static final String PAUSE_ICON = "/gui2/images/message-icons/pause.png";

    @FXML protected Label messageContentLabel;
    @FXML protected Label messageIdLabel;
    @FXML protected Button removeButton;
    @FXML protected ImageButton toggleButton;

    private final ObjectProperty<Message> messageProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<ActionEvent>> onResumeProp = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<ActionEvent>> onPauseProp = new SimpleObjectProperty<>();

    public TopicCard() {
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/TopicCard.fxml", this);

        messageProperty().addListener(new ChangeListener<Message>() {
            @Override
            public void changed(ObservableValue<? extends Message> observableValue, Message oldMessage, Message newMessage) {
                update(newMessage);
            }
        });
        toggleButton.setImage(PLAY_ICON);
    }

    public TopicCard(Message topic) {
        this();
        setMessage(topic);
    }

    @FXML protected void onLink(ActionEvent event) {
        Gui2Utils.copyToClipboard(messageIdLabel.getText());
    }

    @FXML protected void onRemove(ActionEvent event) {
        System.out.println("remove " + getMessage());
    }

    @FXML protected void onToggle(ActionEvent event) {
        if(toggleButton.getImage().equals(PLAY_ICON)) {
            toggleButton.setImage(PAUSE_ICON);
            if(getOnResume() != null) {
                getOnResume().handle(event);
            }
        } else {
            toggleButton.setImage(PLAY_ICON);
            if(getOnPause() != null) {
                getOnPause().handle(event);
            }
        }
    }

    private void update(Message message) {
        messageContentLabel.setText(message.getContent());
        messageIdLabel.setText(message.getIdentifier().toString());
        setColor(DeterministicColorGenerator.get(message));
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

    public ObjectProperty<EventHandler<ActionEvent>> onRemoveProperty() {
        return removeButton.onActionProperty();
    }

    public void setOnRemove(EventHandler<ActionEvent> onRemove) {
        onRemoveProperty().set(onRemove);
    }

    public EventHandler<ActionEvent> getOnRemove() {
        return onRemoveProperty().get();
    }

    public ObjectProperty<EventHandler<ActionEvent>> onResumeProperty() {
        return onResumeProp;
    }

    public EventHandler<ActionEvent> getOnResume() {
        return onResumeProperty().get();
    }

    public void setOnResume(EventHandler<ActionEvent> onResume) {
        onResumeProperty().set(onResume);
    }

    public ObjectProperty<EventHandler<ActionEvent>> onPauseProperty() {
        return onPauseProp;
    }

    public EventHandler<ActionEvent> getOnPause() {
        return onPauseProperty().get();
    }

    public void setOnPause(EventHandler<ActionEvent> onPause) {
        onPauseProperty().set(onPause);
    }
}