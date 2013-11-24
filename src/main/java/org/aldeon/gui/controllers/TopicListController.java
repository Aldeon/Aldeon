package org.aldeon.gui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 */
public class TopicListController extends VBox implements Initializable, ResponseControlListener {
    public VBox mainWindow;
    public TextField topicName;
    private TopicMsgsController topicList;
    private MainController mainController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../TopicMsgs.fxml"));
        Parent parent=null;
        try {
            parent = (Parent) loader.load(getClass().getResource("../TopicMsgs.fxml").openStream());
        } catch (IOException e) {
        }
        topicList = (TopicMsgsController) loader.<TopicMsgsController>getController();
        for (int i = 0; i < 10; i++) {
            topicList.appendMsg("topic " + i, 0, this);
        }
        mainWindow.getChildren().add(parent);
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
}
