package org.aldeon.gui2.components;

import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.aldeon.gui2.Gui2Utils;

public class MessageContent extends BorderPane{

    @FXML protected Label messageContentLabel;
    @FXML protected Label messageIdLabel;
    @FXML protected Label userNameLabel;
    @FXML protected Label userIdLabel;
    @FXML protected ImageView linkIcon;

    public MessageContent() {
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/MessageContent.fxml", this);

        linkIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                copyHashToClipboard();
            }
        });
    }

    private void copyHashToClipboard() {
        ClipboardContent content = new ClipboardContent();
        content.putString(getMessageHash());
        Clipboard.getSystemClipboard().setContent(content);
    }

    public StringProperty textProperty() {
        return messageContentLabel.textProperty();
    }

    public String getText() {
        return textProperty().get();
    }

    public void setText(String text) {
        textProperty().set(text);
    }

    public StringProperty messageHashProperty() {
        return messageIdLabel.textProperty();
    }

    public String getMessageHash() {
        return messageHashProperty().get();
    }

    public void setMessageHash(String messageHash) {
        messageHashProperty().set(messageHash);
    }

    public StringProperty authorNameProperty() {
        return userNameLabel.textProperty();
    }

    public String getUserName() {
        return authorNameProperty().get();
    }

    public void setAuthorName(String authorName) {
        authorNameProperty().set(authorName);
    }

    public StringProperty authorHashProperty() {
        return userIdLabel.textProperty();
    }

    public String getAuthorHash() {
        return authorHashProperty().get();
    }

    public void setAuthorHash(String authorHash) {
        authorHashProperty().set(authorHash);
    }
}
