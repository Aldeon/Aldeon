package org.aldeon.gui2.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.aldeon.gui2.Gui2Utils;
import org.aldeon.gui2.components.HorizontalColorContainer;
import org.aldeon.gui2.components.MessageCard;
import org.aldeon.model.Identifier;

import java.util.HashMap;
import java.util.Map;

public class TopicsController {

    public static final String FXML_FILE = "views/Topics.fxml";

    // Topics view elements
    @FXML protected TextField watchTopicTextField;
    @FXML protected Button watchTopicButton;
    @FXML protected VBox topics;

    // Topic containers
    protected Map<Identifier, HorizontalColorContainer> topicContainers = new HashMap<>();

    public TopicsController() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                HorizontalColorContainer c = new HorizontalColorContainer();
                c.setContent(new MessageCard());
                c.setColor(Color.AQUA);
                addContainer(c);
            }
        });
    }

    // Container management helper methods
    protected void addContainer(HorizontalColorContainer container) {
        if(container != null) {
            topics.getChildren().add(container);
        }
    }

    protected void delContainer(HorizontalColorContainer container) {
        topics.getChildren().remove(container);
    }

    // Actual topic management
    protected void addTopicToList(Identifier topic) {

    }

    protected void removeTopicFromList(Identifier topic) {

    }

    public static Node create() {
        return Gui2Utils.loadFXMLfromDefaultPath(FXML_FILE);
    }
}
