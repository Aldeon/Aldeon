package org.aldeon.gui.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.aldeon.core.CoreModule;
import org.aldeon.gui.GUIController;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    public VBox sidebar;
    public StackPane logo;
    public GridPane content;
    public StackPane Identities;
    public StackPane Threads;
    public StackPane Friends;
    public StackPane Settings;
    public BorderPane main;
    private int optCount;

    public void createCheckboxOption(String option) throws Exception{
        CheckBox setting = new CheckBox(option);
        setting.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                CheckBox option = (CheckBox) e.getTarget();
                CoreModule.getInstance().getPropertiesManager().setProperty(option.getText(), option.selectedProperty());
            }
        });
        setting.setStyle("-fx-font:12px Verdana; -fx-text-fill:#ffffff; ");
        if(Boolean.parseBoolean(CoreModule.getInstance().getPropertiesManager().getProperty(option))==true)
                setting.setSelected(true);
        content.add(setting, 0, optCount);
        optCount++;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // Settings.setStyle("-fx-background-color:linear-gradient(from 0% 0% to 100% 0%, #333333, #333333 90%, #1b1b1b 100%);");
        optCount=1;
        try{
            createCheckboxOption("Setting1");
            createCheckboxOption("Setting2");
        }
        catch(Exception e){}
    }

}

