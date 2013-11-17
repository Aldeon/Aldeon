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
public class TopicListController extends VBox implements Initializable {
    public VBox mainWindow;
    public TextField topicName;
    private TopicMsgsController topicList;

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
            topicList.appendMsg("topic " + i, 0);
        }
        mainWindow.getChildren().add(parent);
    }


    public void createTopic(MouseEvent event){
        topicList.appendMsg(topicName.getText(), 0);
    }


}
