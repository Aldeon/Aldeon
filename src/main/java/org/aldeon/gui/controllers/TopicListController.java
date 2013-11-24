package org.aldeon.gui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 */
public class TopicListController extends VBox implements Initializable, TopicControlListener {
    public VBox mainWindow;
    public TextField topicName;
    private MainController mainController;
    public FlowPane fpane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for ( int i = 0; i < 7; i++)
            fpane.getChildren().add(createTopic("topic with very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very very long first message " + i));
    }

    public Parent createTopic(String topicText) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../Topic.fxml"));
        Parent parent=null;
        try {
            parent = (Parent) loader.load(getClass().getResource("../Topic.fxml").openStream());
        } catch (IOException e) {
        }
        TopicController tc = (TopicController) loader.<TopicController>getController();
        tc.setTopicText(topicText);
        tc.registerListener(this);
        tc.setTopicNode(parent);

        return parent;
    }

    public void createTopic(MouseEvent event){
        fpane.getChildren().add(createTopic(topicName.getText()));
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void topicClicked(String topicText) {
        mainController.showTopicMsgs(topicText);
    }

    @Override
    public void deleteTopicClicked(Parent topicNode) {
        fpane.getChildren().remove(topicNode);
        //TODO notify DB
    }
}
