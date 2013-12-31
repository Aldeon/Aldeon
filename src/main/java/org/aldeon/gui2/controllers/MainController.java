package org.aldeon.gui2.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
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
    private int activeId = 0;

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

    private void setContent(Node node, int id) {
        if(node != active && node != null) {
            Direction dir = Direction.BOTTOM;
            if(id > activeId) dir = dir.opposite();
            content.slideOut(active, dir);
            content.slideIn(node, dir.opposite());
            active = node;
            activeId = id;
        }
    }

    @FXML protected void clickedLogo(MouseEvent event) {
        setContent(welcomeNode, 0);
    }

    @FXML protected void clickedIdentities(MouseEvent event) {
        setContent(identitiesNode, 1);
    }

    @FXML protected void clickedTopics(MouseEvent event) {
        setContent(topicsNode, 2);
    }

    @FXML protected void clickedFriends(MouseEvent event) {
        setContent(friendsNode, 3);
    }

    @FXML protected void clickedSettings(MouseEvent event) {
        setContent(settingsNode, 4);
    }
}


