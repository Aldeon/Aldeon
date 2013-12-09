package org.aldeon.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.aldeon.core.CoreModule;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

/**
 *
 */
public class TopicListController implements Initializable, TopicControlListener {
    public VBox mainWindow;
    public TextField topicName;
    private MainController mainController;
    public FlowPane fpane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CoreModule.getInstance().getStorage().getMessagesByParentId(Identifier.empty(),
                new Callback<Set<Message>>() {
                    @Override
                    public void call(Set<Message> val) {
                        for (Message message : val) {
                            final Message m = message;

//the code below will be executed by FX thread. Only that thread can update GUI elements.
//any attempt of doing this in another thread will cause "Not on FX application thread" exception
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    fpane.getChildren().add(createTopic(m));
                                }
                            });
                        }
                    }
                });
    }

    public Parent createTopic(Message message) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/gui/fxml/Topic.fxml"));
        Parent parent=null;
        try {
            parent = (Parent) loader.load(getClass().getResource("/gui/fxml/Topic.fxml").openStream());
        } catch (IOException e) {
        }
        TopicController tc = loader.getController();
        tc.setMessage(message);
        tc.registerListener(this);
        tc.setTopicNode(parent);

        //dodawanie wiadomosci
        //dodawanie topicow to to samo tylko ze dodatkowo dajemy parent empty
// CoreModule.getInstance().getEventLoop().notify(new MessageAddedEvent(
// Messages.createAndSign(, ,,)
// ));

        //dodawanie identity
        // notify identityAdded
        // new Identity(String name);

        return parent;
    }

    public void createTopic(MouseEvent event){
        //TODO fire topic subscription event
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void topicClicked(Message topicMessage) {
        mainController.showTopicMsgs(topicMessage);
    }

    @Override
    public void deleteTopicClicked(Parent topicNode) {
        fpane.getChildren().remove(topicNode);
        //TODO notify DB
    }
}
