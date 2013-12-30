package org.aldeon.gui2.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import org.aldeon.gui2.Gui2Utils;
import org.aldeon.gui2.various.DeterministicColorGenerator;
import org.aldeon.model.Message;

public class MessageCard extends HorizontalColorContainer {

    @FXML protected MessageContent messageData;

    private final ObjectProperty<Message> messageProperty = new SimpleObjectProperty<>();

    public MessageCard() {
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/MessageCard.fxml", this);

        messageProperty().addListener(new ChangeListener<Message>() {
            @Override
            public void changed(ObservableValue<? extends Message> observableValue, Message oldMessage, Message newMessage) {
                messageData.setAuthorName("Anonymous");
                messageData.setAuthorHash(newMessage.getAuthorPublicKey().toString());
                messageData.setMessageHash(newMessage.getIdentifier().toString());
                messageData.setText(newMessage.getContent());
                setColor(DeterministicColorGenerator.get(newMessage.getAuthorPublicKey().hashCode()));
            }
        });
    }

    public MessageCard(Message message) {
        this();
        setMessage(message);
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
