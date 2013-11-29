package org.aldeon.gui.controllers;

import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 */
public class TopicController extends HBox {
    public HBox topicBox;
    public Rectangle colorRectangle;
    public Rectangle backgroundRectangle;
    public Text topicText;

    private String fullTopicText;
    private TopicControlListener listener;
    private Parent topicNode;

    public void registerListener(TopicControlListener topicControlListener) {
        listener = topicControlListener;
    }


    public void setTopicText(String topicText) {
        fullTopicText = topicText;

        if (topicText.length() > 60) {
            this.topicText.setText(topicText.substring(0,59) + "...");
        }
        else this.topicText.setText(topicText);

    }
    public void topicTextClicked(MouseEvent event) {
        if (listener != null) listener.topicClicked(fullTopicText);
    }

    public void setTopicNode(Parent topicNode) {
        this.topicNode = topicNode;
    }

    public void deleteTopicClicked(MouseEvent event) {
        if (listener != null) listener.deleteTopicClicked(topicNode);
    }
}
