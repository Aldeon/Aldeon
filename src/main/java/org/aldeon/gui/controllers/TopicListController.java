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
public class TopicListController extends VBox implements Initializable, ResponseControlListener, TopicControlListener {
    public VBox mainWindow;
    public TextField topicName;
    private TopicMsgsController topicList;
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

        return parent;
    }

    public void createTopic(MouseEvent event){
        topicList.appendMsg(topicName.getText(), 0, this);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void responseClicked(ResponseController rc, String text) {
        System.out.println("the response was clicked!");
        mainController.showTopicMsgs(text);
    }

    @Override
    public void responseRespondClicked(Parent rc) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void responseDeleteClicked(ResponseController rc) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void topicClicked(String topicText) {
        mainController.showTopicMsgs(topicText);
        //load topic related stuff (msgs with that topic parent id)
        //or tell main controller that it needs to load topic msgs with that topic parent id
    }
}
