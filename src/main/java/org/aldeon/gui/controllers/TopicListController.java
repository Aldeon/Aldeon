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
import org.aldeon.utils.helpers.Callbacks;
import org.aldeon.utils.helpers.Identifiers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    private ArrayList<Identifier> topicsSyncing = new ArrayList<Identifier>();

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

        return parent;
    }

    public void createTopic(MouseEvent event){
        //TODO helper function checking if identifier is in valid format
        Identifier topic = Identifiers.fromBase64(topicName.getText());
        if (isTopicInSync(topic)) {
            System.out.println("topic is syncing");
        } else {
            topicsSyncing.add(topic);
            CoreModule.getInstance().getTopicManager().addTopic(topic);
        }
    }

    private boolean isTopicInSync(Identifier identifier) {
        for (Identifier i : topicsSyncing) {
            if (i.equals(identifier)) return true;
        }
        return false;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void topicClicked(Message topicMessage) {
        mainController.showTopicMsgs(topicMessage);
    }

    public void createNewTopic(MouseEvent event) {
        mainController.showCreateThreadView();
    }

    @Override
    public void deleteTopicClicked(Parent topicNode, Message topicMessage) {
        CoreModule.getInstance().getStorage().deleteMessage(topicMessage.getIdentifier(), Callbacks.<Boolean>emptyCallback());
        fpane.getChildren().remove(topicNode);
    }
}
