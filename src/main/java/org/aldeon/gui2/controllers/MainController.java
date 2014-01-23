package org.aldeon.gui2.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.aldeon.gui2.components.SlidingStackPane;
import org.aldeon.gui2.components.TreeConversationViewer;
import org.aldeon.gui2.various.Direction;

import java.util.HashMap;
import java.util.Map;

public class MainController {

    private static MainController instance;
    @FXML private SlidingStackPane content;

    private int activeId = 0;
    private Map<Integer, SlidingStackPane> sliders = new HashMap<>();

    public static final int CURRENT_SLIDE_PANE = 0;
    public static final int WELCOME_SLIDE_PANE = 1;
    public static final int IDENTITIES_SLIDE_PANE = 2;
    public static final int TOPICS_SLIDE_PANE = 3;
    public static final int FRIENDS_SLIDE_PANE = 4;
    public static final int SETTINGS_SLIDE_PANE = 5;

    public MainController() {
        // Allocate all view nodes
        sliders.put(WELCOME_SLIDE_PANE, createSlidingStackPane(WelcomeController.create()));
        sliders.put(TOPICS_SLIDE_PANE, createSlidingStackPane(TopicsController.create()));
        sliders.put(FRIENDS_SLIDE_PANE, createSlidingStackPane(FriendsController.create()));
        sliders.put(IDENTITIES_SLIDE_PANE, createSlidingStackPane(IdentitiesController.create()));
        sliders.put(SETTINGS_SLIDE_PANE, createSlidingStackPane(SettingsController.create()));

        instance = this;
    }

    private SlidingStackPane createSlidingStackPane(Node node) {
        SlidingStackPane pane = new SlidingStackPane();
        pane.insertWithoutAnimation(node);
        return pane;
    }

    /**
     * "second constructor" - fired as soon as all the Node references are set.
     */
    public void initialize() {
        content.insertWithoutAnimation(getSlider(WELCOME_SLIDE_PANE));
        activeId = WELCOME_SLIDE_PANE;
    }

    @FXML protected void clickedLogo(MouseEvent event) {
        slideTo(WELCOME_SLIDE_PANE);
    }

    @FXML protected void clickedIdentities(MouseEvent event) {
        slideTo(IDENTITIES_SLIDE_PANE);
    }

    @FXML protected void clickedTopics(MouseEvent event) {
        slideTo(TOPICS_SLIDE_PANE);
    }

    @FXML protected void clickedFriends(MouseEvent event) {
        slideTo(FRIENDS_SLIDE_PANE);
    }

    @FXML protected void clickedSettings(MouseEvent event) {
        slideTo(SETTINGS_SLIDE_PANE);
    }

    public static MainController getInstance() {
        return instance;
    }

    public void slideTo(int slidePane) {
        SlidingStackPane pane = getSlider(slidePane);

        if(slidePane != activeId &&  pane != null) {
            Direction dir = Direction.BOTTOM;
            if(slidePane > activeId) dir = dir.opposite();
            content.slideOut(getSlider(activeId), dir);
            content.slideIn(pane, dir.opposite());
            activeId = slidePane;
        }
    }
    public SlidingStackPane getSlider(int slidePane) {
        if (slidePane == CURRENT_SLIDE_PANE) {
            return sliders.get(activeId);
        }
        return sliders.get(slidePane);
    }
}


