package org.aldeon.gui2.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.aldeon.gui2.components.SlidingStackPane;
import org.aldeon.gui2.various.Direction;

public class MainController {

    // Node references
    @FXML private SlidingStackPane content;

    // View nodes (allocated once)
    private Node welcomeNode    = null;
    private Node identitiesNode = null;
    private Node topicsNode     = null;
    private Node friendsNode    = null;
    private Node settingsNode   = null;
    private Node active = null;

    public MainController() {
        // Allocate all view nodes
        welcomeNode = WelcomeController.create();
        topicsNode = TopicsController.create();
        identitiesNode = IdentitiesController.create();
    }

    /**
     * "second constructor" - fired as soon as all the Node references are set.
     */
    public void initialize() {
        content.getChildren().add(welcomeNode);
        active = welcomeNode;
    }

    private void setContent(Node node) {
        if(node != active && node != null) {
            content.slideOut(active, Direction.TOP);
            content.slideIn(node, Direction.RIGHT);
            active = node;
        }
    }

    @FXML protected void clickedLogo(MouseEvent event) {
        setContent(welcomeNode);
    }

    @FXML protected void clickedIdentities(MouseEvent event) {
        setContent(identitiesNode);
    }

    @FXML protected void clickedTopics(MouseEvent event) {
        setContent(topicsNode);
    }

    @FXML protected void clickedFriends(MouseEvent event) {
        setContent(friendsNode);
    }

    @FXML protected void clickedSettings(MouseEvent event) {
        setContent(settingsNode);
    }
}


