package org.aldeon.gui2.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.aldeon.gui2.Gui2Utils;
import org.aldeon.gui2.various.DeterministicColorGenerator;
import org.aldeon.model.Message;

public class TopicCard extends HorizontalColorContainer {

    @FXML protected Label messageContentLabel;
    @FXML protected Label messageIdLabel;

    private final ObjectProperty<Message> messageProperty = new SimpleObjectProperty<>();

    public TopicCard() {
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/TopicCard.fxml", this);

        messageProperty().addListener(new ChangeListener<Message>() {
            @Override
            public void changed(ObservableValue<? extends Message> observableValue, Message oldMessage, Message newMessage) {
                update(newMessage);
            }
        });
    }

    public TopicCard(Message topic) {
        this();
        setMessage(topic);
    }

    @FXML protected void onLink(ActionEvent event) {
        Gui2Utils.copyToClipboard(messageIdLabel.getText());
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
}
