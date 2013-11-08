package org.aldeon.gui.controllers;

import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.aldeon.gui.GUIController;

import java.net.URL;
import java.util.ResourceBundle;


public class MainController extends BorderPane implements Initializable {
    public VBox sidebar;
    public StackPane logo;
    public GridPane content;
    public StackPane Identities;
    public StackPane Threads;
    public StackPane Friends;
    public StackPane Settings;
    private GUIController root;

    public void changeMode(MouseEvent event) throws Exception{
        root.changeMode(event);
    }

    public void setRoot(GUIController root){
        this.root=root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

}


