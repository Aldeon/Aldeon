package org.aldeon.gui.listeners;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.aldeon.gui.GUIManager;

public class ThreadCreator implements EventHandler {
    private StackPane searchBox;
    private int threadCount;
    private GUIManager gui;
    public ThreadCreator(StackPane parent, GUIManager manager){
        searchBox=parent;
        threadCount=0;
        gui=manager;
    }

    //Wystarczy przeciazyc wymiarami i bedzie z luzem na jednej
    public StackPane newThread(String id, Paint clr){
        StackPane newThread = new StackPane();
        Rectangle field = new Rectangle(400,50);
        field.setArcHeight(10);
        field.setArcWidth(10);
        field.setFill(clr);
        Rectangle top = new Rectangle(370,50);
        top.setFill(Color.web("#222222"));
        Text name = new Text(id);
        name.setFont(new Font("Verdana",12));
        name.setFill(Color.web("#ffffff"));
        newThread.setAlignment(Pos.CENTER);
        newThread.getChildren().add(field);
        newThread.getChildren().add(top);
        newThread.setAlignment(name, Pos.CENTER_LEFT);
        StackPane.setMargin(name, new Insets(0, 0, 0, 30));
        newThread.getChildren().add(name);
        ImageView remove = new ImageView(getClass().getResource("\\..\\resource\\cross.png").toExternalForm());
        newThread.setAlignment(remove,Pos.TOP_RIGHT);
        StackPane.setMargin(remove,new Insets(10,30,0,0));
        //remove.setOnMousePressed(this);              //TODO: remove thread -> Observer-listener jakiś
        newThread.getChildren().add(remove);
        threadCount++;
        newThread.setOnMousePressed(new ThreadInspector(newThread, gui));
        return newThread;
    }


    @Override
    public void handle(Event event) {
        StackPane sth=newThread(((TextField)searchBox.getChildren().get(0)).getText(),Color.web("#ffffff"));          //Color based on a fair dice roll
        ((GridPane)searchBox.getParent()).add(sth, 0, threadCount);
    }
}
