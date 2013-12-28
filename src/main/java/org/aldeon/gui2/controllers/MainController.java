package org.aldeon.gui2.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class MainController {

    // Node references
    @FXML private StackPane content;

    // View nodes (allocated once)
    private Node welcomeNode    = null;
    private Node identitiesNode = null;
    private Node topicsNode     = null;
    private Node friendsNode    = null;
    private Node settingsNode   = null;

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
        setContent(welcomeNode);
    }

    private void setContent(Node node) {
        content.getChildren().clear();
        if(node != null) {
            content.getChildren().add(node);
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


