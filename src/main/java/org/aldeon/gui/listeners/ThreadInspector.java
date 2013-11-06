package org.aldeon.gui.listeners;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.aldeon.gui.GUIManager;

public class ThreadInspector implements EventHandler {
    private StackPane stack;
    private int respCount;
    private GUIManager gui;
    public ThreadInspector(StackPane parent, GUIManager manager){
        stack=parent;
        respCount=0;
        gui=manager;
    }

    public StackPane addResponse(String content, String user){
        StackPane resp = new StackPane();
        Rectangle field = new Rectangle(350,50);
        field.setArcHeight(10);
        field.setArcWidth(10);
        field.setFill(Color.web("#ffffff"));
        Rectangle top = new Rectangle(320,50);
        top.setFill(Color.web("#222222"));
        Text name = new Text(content);
        name.setFont(new Font("Verdana",12));
        name.setFill(Color.web("#ffffff"));
        resp.setAlignment(Pos.CENTER);
        resp.getChildren().add(field);
        resp.getChildren().add(top);
        resp.setAlignment(name, Pos.CENTER_LEFT);
        StackPane.setMargin(name, new Insets(0, 0, 0, 30));
        resp.getChildren().add(name);
        ImageView remove = new ImageView(getClass().getResource("\\..\\resource\\cross.png").toExternalForm());
        resp.setAlignment(remove, Pos.TOP_RIGHT);
        StackPane.setMargin(remove,new Insets(10,30,0,0));
        //remove.setOnMousePressed(this);              //TODO: remove thread -> Observer-listener jakiś
        resp.getChildren().add(remove);
        respCount++;
        return resp;
    }

    //Don't even think about looking in here
    public void mockupThread(){
        GridPane pane = new GridPane();
        pane.setVgap(20);
        pane.setHgap(20);
        pane.setStyle("-fx-background-color:#444444;");
        pane.setPadding(new Insets(30, 0, 0, 30));
        StackPane newThread = new StackPane();
        Rectangle field = new Rectangle(400,50);
        field.setArcHeight(10);
        field.setArcWidth(10);
        field.setFill(Color.web("#ffffff"));
        Rectangle top = new Rectangle(370,50);
        top.setFill(Color.web("#222222"));
        Text name = (Text)(stack.getChildren().get(2));
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
        pane.add(newThread, 0, 0);
        StackPane resp1=addResponse("BLAH CONTENT","BLAH USER");
        GridPane.setMargin(resp1,new Insets(0,0,0,50));
        pane.add(resp1,0,respCount);
        StackPane resp2=addResponse("BLAH CONTENT","BLAH USER");
        GridPane.setMargin(resp2,new Insets(0,0,0,50));
        pane.add(resp2, 0, respCount);
        pane.setPrefSize(600,600);
        gui.changeMain(pane);
    }

    @Override
    public void handle(Event event) {
        mockupThread();
    }
}
