package org.aldeon.gui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.aldeon.gui.GUIController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainController extends BorderPane implements Initializable {
    public VBox sidebar;
    public StackPane logo;
    public Pane contents;
    public StackPane Identities;
    public StackPane Threads;
    public StackPane Friends;
    public StackPane Settings;
    private GUIController root;

    private void changeFxml(String pathToFxml) {
        contents.getChildren().clear();
        try {
            contents.getChildren().add((Node)FXMLLoader.load(getClass().getResource(pathToFxml)));
        } catch (IOException e) {
            //invalid path
            e.printStackTrace();
        }
    }
    public void changeMode(MouseEvent event) throws Exception{

        String target="";
        if(event.getTarget().getClass()!=StackPane.class){
            if(event.getTarget().getClass()==ImageView.class){
                target=(((ImageView)event.getTarget()).getParent()).getId();
            }
            if(event.getTarget().getClass()==Text.class){
                target=(((Text)event.getTarget()).getParent()).getId();
            }
        }else{
            target=((StackPane)event.getTarget()).getId();
        }

        switch(target){
            case "Identities":
                changeFxml("../Identities.fxml");
                break;
            case "Threads":
                changeFxml("../Threads.fxml");
                break;
            case "Friends":
                changeFxml("../Friends.fxml");
                break;
            case "Settings":
                changeFxml("../Settings.fxml");
                break;
        }
    }

    public void setRoot(GUIController root){
        this.root=root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

}


