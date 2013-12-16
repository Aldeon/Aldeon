package org.aldeon.gui.controllers;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.aldeon.gui.colors.ColorManager;
import org.aldeon.model.Message;

/**
 *
 */
public class TopicController {
    public HBox topicBox;
    public Rectangle colorRectangle;
    public Rectangle backgroundRectangle;
    public Text topicText;
    public Insets x1;
    public Text topicHash;

    private String fullTopicText;
    private TopicControlListener listener;
    private Parent topicNode;
    private Message message;

    public void registerListener(TopicControlListener topicControlListener) {
        listener = topicControlListener;
    }

    public void setMessage(Message m) {
        message = m;
        colorRectangle.setFill(ColorManager.getColorForKey(m.getAuthorPublicKey()));
        topicHash.setText(m.getIdentifier().toString());

        setTopicText(m.getContent());
    }

    private void setTopicText(String topicText) {
        fullTopicText = topicText;

        if (topicText.length() > 60) {
            this.topicText.setText(topicText.substring(0,59) + "...");
        }
        else this.topicText.setText(topicText);

    }
    public void topicTextClicked(MouseEvent event) {
        if (listener != null) listener.topicClicked(message);
    }

    public void setTopicNode(Parent topicNode) {
        this.topicNode = topicNode;
    }

    public void deleteTopicClicked(MouseEvent event) {
        if (listener != null) listener.deleteTopicClicked(topicNode, message);
    }

    public void linkIconClicked(MouseEvent event) {
        final ClipboardContent content = new ClipboardContent();
        content.putString(message.getIdentifier().toString());
        Clipboard.getSystemClipboard().setContent(content);
    }
}
