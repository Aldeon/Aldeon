package org.aldeon.gui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.aldeon.gui.GuiUtils;
import org.aldeon.gui.GUIController;
import org.aldeon.model.Message;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    public VBox sidebar;
    public StackPane logo;
    public ScrollPane contents;
    public StackPane Identities;
    public StackPane Threads;
    public StackPane Friends;
    public StackPane Settings;
    public ImageView IdentitiesImage;
    public ImageView ThreadsImage;
    public ImageView FriendsImage;
    public ImageView SettingsImage;
    private GUIController root;

    private void changeFxml(String pathToFxml) {
        Node node = GuiUtils.loadFxml(pathToFxml);
        contents.setContent(node);
    }

    private void resetHighlights(StackPane stackPane, ImageView imageView) {
        int index = stackPane.getStyleClass().indexOf("menuhighlight");
        if (index != -1) {
            stackPane.getStyleClass().remove(index);
            stackPane.getStyleClass().add("menuNoHighlight");
            imageView.opacityProperty().set(0.3);
        }
    }

    private void setHighlights(StackPane stackPane, ImageView imageView) {
        imageView.opacityProperty().set(0.8);
        stackPane.getStyleClass().add("menuhighlight");

        int index = stackPane.getStyleClass().indexOf("menuNoHighlight");
        if (index != -1) {
            stackPane.getStyleClass().remove(index);
        }
    }

    private void resetAllHighlights() {
        resetHighlights(Identities, IdentitiesImage);
        resetHighlights(Threads, ThreadsImage);
        resetHighlights(Friends, FriendsImage);
        resetHighlights(Settings, SettingsImage);
    }

    public void identitiesClicked(MouseEvent event) {
        resetAllHighlights();
        setHighlights(Identities, IdentitiesImage);
        changeFxml("Identities.fxml");
    }

    public void threadsClicked(MouseEvent event) {
        resetAllHighlights();
        setHighlights(Threads, ThreadsImage);
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/gui/fxml/TopicList.fxml"));
        Parent parent = null;
        try {
            parent = (Parent) loader.load(getClass().getResource("/gui/fxml/TopicList.fxml").openStream());
        } catch (IOException e) {
        }
        TopicListController topicListController = loader.getController();
        topicListController.setMainController(this);

        contents.setContent(parent);
    }

    public void friendsClicked(MouseEvent event) {
        resetAllHighlights();
        setHighlights(Friends, FriendsImage);
        changeFxml("Friends.fxml");
    }

    public void settingsClicked(MouseEvent event) {
        resetAllHighlights();
        setHighlights(Settings, SettingsImage);
        changeFxml("Settings.fxml");
    }


    public void showTopicMsgs(Message rootMsg) {
        //TODO pass root msg identifier in argument
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/gui/fxml/TopicMsgs.fxml"));
        Parent parent=null;
        try {
            parent = (Parent) loader.load(getClass().getResource("/gui/fxml/TopicMsgs.fxml").openStream());
        } catch (IOException e) {
        }
        TopicMsgsController topicMsgsController = loader.getController();
        topicMsgsController.setTopicMessage(rootMsg);

        contents.setContent(parent);
    }

    public void setRoot(GUIController root){
        this.root = root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeFxml("dashboard.fxml");
    }

}


