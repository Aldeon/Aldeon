package org.aldeon.gui2.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.aldeon.events.Callback;
import org.aldeon.gui2.Gui2Utils;
import org.aldeon.gui2.components.MessageCard;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TopicsController {

    public static final String FXML_FILE = "views/Topics.fxml";

    // Topics view elements
    @FXML protected TextField watchTopicTextField;
    @FXML protected Button watchTopicButton;
    @FXML protected VBox topics;

    // Topic containers
    protected Map<Identifier, MessageCard> topicContainers = new HashMap<>();

    public void initialize() {
        fetchTopics();
    }

    private void fetchTopics() {
        Gui2Utils.db().getMessagesByParentId(Identifier.empty(), new Callback<Set<Message>>() {
            @Override
            public void call(Set<Message> topics) {
                for(Message topic: topics) {
                    addTopicToList(topic);
                }
            }
        });
    }

    protected void addTopicToList(Message topic) {
        if(!topicContainers.containsKey(topic.getIdentifier())) {
            MessageCard card = new MessageCard();
            card.setMessage(topic);
            topics.getChildren().add(card);
            topicContainers.put(topic.getIdentifier(), card);
        }
    }

    protected void removeTopicFromList(Message topic) {
        MessageCard card = topicContainers.get(topic.getIdentifier());
        if(card != null) {
            topicContainers.remove(topic.getIdentifier());
            topics.getChildren().remove(card);
        }
    }

    public static Node create() {
        return Gui2Utils.loadFXMLfromDefaultPath(FXML_FILE);
    }
}
