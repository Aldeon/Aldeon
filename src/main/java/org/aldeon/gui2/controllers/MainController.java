package org.aldeon.gui2.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.aldeon.gui2.components.ColorizedImageView;

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
        identitiesNode = IdentitiesController.create();

        ColorizedImageView img = new ColorizedImageView("/gui2/images/person-icon.png");
        img.setColorize(Color.CHOCOLATE);

        settingsNode = img;
    }

    public void initialize() {
        setContent(welcomeNode);
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


