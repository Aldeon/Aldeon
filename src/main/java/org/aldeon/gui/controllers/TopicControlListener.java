package org.aldeon.gui.controllers;

import javafx.scene.Parent;
import org.aldeon.model.Message;

/**
 *
 */
public interface TopicControlListener {
    void topicClicked(Message topicMessage);
    void deleteTopicClicked(Parent topicNode, Message topicMessage);
}
