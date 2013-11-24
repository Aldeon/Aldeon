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
    private TopicMsgsController topicList;
    private MainController mainController;
    public FlowPane fpane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        FXMLLoader loader = new FXMLLoader(
//                getClass().getResource("../TopicMsgs.fxml"));
//        Parent parent=null;
//        try {
//            parent = (Parent) loader.load(getClass().getResource("../TopicMsgs.fxml").openStream());
//        } catch (IOException e) {
//        }
//        topicList = (TopicMsgsController) loader.<TopicMsgsController>getController();
//        for (int i = 0; i < 10; i++) {
//            topicList.appendMsg("topic " + i, 0, this);
//        }
//        mainWindow.getChildren().add(parent);

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
        //topicList = (TopicMsgsController) loader.<TopicMsgsController>getController();
        //for (int i = 0; i < 10; i++) {
        //    topicList.appendMsg("topic " + i, 0, this);
        //}
        //mainWindow.getChildren().add(parent);

        return parent;
    }

    public void createTopic(MouseEvent event){
        //topicList.appendMsg(topicName.getText(), 0, this);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void topicClicked(String topicText) {
        mainController.showTopicMsgs(topicText);
        //load topic related stuff (msgs with that topic parent id)
        //or tell main controller that it needs to load topic msgs with that topic parent id
    }
}
