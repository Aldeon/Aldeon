package org.aldeon.gui.listeners;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.aldeon.gui.GUIManager;

public class OnOptionClicked implements EventHandler {
    private StackPane parent;
    private GUIManager gui;

    public OnOptionClicked(StackPane stack, GUIManager manager){
        parent=stack;
        gui=manager;
    }

    @Override
    public void handle(Event event) {
        String choice = ((Text)parent.getChildren().get(2)).getText();
        if(choice=="")choice="main";
        gui.changeMain(gui.setMainPane(choice));
    }
}
