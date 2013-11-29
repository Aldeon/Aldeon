package org.aldeon.gui.controllers;

import javafx.scene.Parent;

/**
 *
 */
public interface TopicControlListener {
    void topicClicked(String topicText);
    void deleteTopicClicked(Parent topicNode);
}
