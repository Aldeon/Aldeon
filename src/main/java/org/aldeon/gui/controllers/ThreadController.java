package org.aldeon.gui.controllers;

import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.aldeon.gui.GUIController;

import java.net.URL;
import java.util.ResourceBundle;


public class ThreadController extends BorderPane implements Initializable {

    public BorderPane main;
    public VBox sidebar;
    public StackPane logo;
    public GridPane content;
    public StackPane Identities;
    public StackPane Threads;
    public StackPane Friends;
    public StackPane Settings;
    public StackPane threadCreator;
    public TextField threadName;
    public Button createButton;
    private GUIController root;
    private int threadCount;

    public void createThread(MouseEvent event){
        content.add(createField(threadName.getText(),false,getColorForId()),0,threadCount);
        threadName.setText("");
    }

    public Color getColorForId(){           //Return color corresponding to current user
        return Color.web("#ffffff");        //Based on a fair dice roll
    }

    public void inspectThread(Text header){   //Root message number to get data from database
        GridPane pane = new GridPane();
        threadCount=0;
        pane.add(createField(header.getText(),true,getColorForId()),0,threadCount);
        pane.add(createResponse("RESP1", "USER1"),0,threadCount);     //Add all responses from database
        pane.add(createResponse("RESP2", "USER1"),0,threadCount);
        pane.add(createResponse("RESP3", "USER1"),0,threadCount);
        pane.setVgap(20);
        pane.setHgap(20);
        pane.setPadding(new Insets(20, 0, 0, 10));
        pane.setId("content");
        content=pane;
        main.setCenter(content);
    }

    public StackPane createResponse(String msg, String user){
        StackPane resp = new StackPane();
        Rectangle field = new Rectangle(350,50);
        field.setArcHeight(10);
        field.setArcWidth(10);
        field.setFill(Color.web("#ffffff"));
        Rectangle top = new Rectangle(320,50);
        top.setFill(Color.web("#222222"));
        Text name = new Text(msg);
        name.setFont(new Font("Verdana",12));
        name.setFill(Color.web("#ffffff"));
        resp.setAlignment(Pos.CENTER);
        resp.getChildren().add(field);
        resp.getChildren().add(top);
        resp.setAlignment(name, Pos.CENTER_LEFT);
        StackPane.setMargin(name, new Insets(0, 0, 0, 45));
        resp.getChildren().add(name);
        threadCount++;
        return resp;
    }

    public StackPane createField(String topic, boolean inspect, Color clr){
        StackPane newThread = new StackPane();
        Rectangle field = new Rectangle(400,50);
        field.setArcHeight(10);
        field.setArcWidth(10);
        field.setFill(clr);
        Rectangle top = new Rectangle(370,50);
        top.setFill(Color.web("#222222"));
        final Text name = new Text(topic);
        name.setFont(new Font("Verdana",12));
        name.setFill(Color.web("#ffffff"));
        newThread.setAlignment(Pos.CENTER);
        newThread.getChildren().add(field);
        newThread.getChildren().add(top);
        newThread.setAlignment(name, Pos.CENTER_LEFT);
        StackPane.setMargin(name, new Insets(0, 0, 0, 20));
        newThread.getChildren().add(name);
        if(!inspect){
            newThread.setOnMousePressed((new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    inspectThread(new Text(name.getText()));
                }
            }));
            StackPane.setMargin(name, new Insets(0, 0, 0, 45));
        }
        threadCount++;
        return newThread;
    }

    public void setRoot(GUIController root){
        this.root=root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        threadCount=1;
        //Threads.setStyle("-fx-background-color:linear-gradient(from 0% 0% to 100% 0%, #333333, #333333 90%, #1b1b1b 100%);");
    }
}

