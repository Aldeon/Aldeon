package org.aldeon.gui2.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.aldeon.events.Callback;
import org.aldeon.gui2.Gui2Utils;
import org.aldeon.gui2.components.ConversationViewer;
import org.aldeon.gui2.components.MessageCard;
import org.aldeon.gui2.components.MessageCreator;
import org.aldeon.gui2.components.SlidingStackPane;
import org.aldeon.gui2.components.TopicCard;
import org.aldeon.gui2.various.Direction;
import org.aldeon.gui2.various.MessageEvent;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TopicsController {

    public static final String FXML_FILE = "views/Topics.fxml";

    // Topics view elements
    @FXML protected SlidingStackPane slider;
    @FXML protected TextField watchTopicTextField;
    @FXML protected Button watchTopicButton;
    @FXML protected VBox topics;

    public void initialize() {
        Gui2Utils.db().getMessagesByParentId(Identifier.empty(), new Callback<Set<Message>>() {
            @Override
            public void call(Set<Message> topicsSet) {
                for (Message topic : topicsSet) {
                    addTopic(topic);
                }
            }
        });
    }

    private void addTopic(final Message topic) {
        TopicCard card = new TopicCard(topic);
        card.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                focusOnTopic(topic);
            }
        });
        topics.getChildren().add(card);
    }

    private void focusOnTopic(Message topic) {
        final ConversationViewer viewer = new ConversationViewer();
        viewer.setFocus(topic);
        viewer.setOnViewerClosed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                slider.slideOut(viewer, Direction.RIGHT);
            }
        });
        slider.slideIn(viewer, Direction.RIGHT);
    }

    public static Node create() {
        return Gui2Utils.loadFXMLfromDefaultPath(FXML_FILE);
    }

    @FXML public void newTopicClicked(ActionEvent actionEvent) {
        final MessageCreator creator = new MessageCreator(Identifier.empty());
        creator.setOnCreatorClosed(new EventHandler<MessageEvent>() {
            @Override
            public void handle(MessageEvent messageEvent) {
                if(messageEvent.message() != null) {
                    // add message to db
                    addTopic(messageEvent.message());
                }
                slider.slideOut(creator, Direction.RIGHT);
            }
        });
        slider.slideIn(creator, Direction.RIGHT);
    }
}
