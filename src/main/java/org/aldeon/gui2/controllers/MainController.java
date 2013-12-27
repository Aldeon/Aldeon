package org.aldeon.gui2.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.aldeon.gui2.components.HorizontalColorContainer;

public class MainController {

    // Reference to the content pane
    @FXML private StackPane content;

    // View nodes (allocated once)
    private Node welcomeNode        = null;
    private Node identitiesNode     = null;
    private Node topicsNode         = null;
    private Node friendsNode        = null;
    private Node settingsNode       = null;

    public MainController() {
        // Allocate all view nodes
        welcomeNode = WelcomeController.create();
        topicsNode = TopicsController.create();
        friendsNode = new HorizontalColorContainer();

        // Display first node
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setContent(welcomeNode);
            }
        });
    }

    private void clearContent() {
        content.getChildren().clear();
    }

    private void addToContent(Node node) {
        if(node != null) {
            content.getChildren().add(node);
        }
    }

    private void setContent(Node node) {
        clearContent();
        addToContent(node);
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


