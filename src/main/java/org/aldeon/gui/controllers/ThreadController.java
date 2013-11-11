package org.aldeon.gui.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.aldeon.gui.GUIController;

import java.io.IOException;
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

    private String weirdText = "1. Przecz skrżytało pogaństwo a ludzie myślili są prozność? \n" +
            "2. Przystajali są krolowie ziemszczy a książęta seszli są sie na gromadę przeciwo Gospodnu i przeciwo \n" +
            "jego pomazańcu. \n" +
            "3. Roztargajmy jich przekowy i srzucimy s nas jarzmo jich. \n" +
            "4. Jen przebywa na niebiesiech, pośmieje sie jim, i Gospodzin zwala śmiech w nich. \n" +
            "5. Tegdy mołwić będzie k nim w gniewie swojem i w rosierdziu swojem zamąci je.  2\n" +
            "6. Ale ja postawion jeśm krol od niego na Syjon, gorze świętej jego, \n" +
            "przepowiadaję kaźń jego. \n" +
            "7. Gospodzin rzekł ku mnie: Syn moj jeś ty, ja dzisia porodził jeśm cie. \n" +
            "8. Pożędaj ote mnie, i dam ci pogany w dziedzicstwo twoje i w trzymanie twoje kraje ziemskie. \n" +
            "9. Włodać będziesz nad nimi w mietle żelaznej a jako ssąd zdunowy \n" +
            "rozbijesz je. \n" +
            "10. A już, krolowie, rozumiejcie, nauczcie sie, cso sądzicie ziemię. \n" +
            "11. Służycie Bogu w strasze i wiesielcie sie jemu se drżenim. \n" +
            "12. Przyjmicie pokaźnienie, bo snadź rozgniewa sie Gospodzin, i \n" +
            "zginiecie z drogi prawej. \n" +
            "13. Gdy rozżgą na krotce gniew jego, błogosławieni wszystcy, jiż imają w niem pwę";

    public void createThread(MouseEvent event){
        content.add(createField(threadName.getText(),false,getColorForId()),0,threadCount);
        threadName.setText("");
    }

    public Color getColorForId(){           //Return color corresponding to current user
        return Color.web("#ffffff");        //Based on a fair dice roll
    }

    private Parent constructResponse(String text) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../Resp.fxml"));
        Parent parent=null;
        try {
            parent = (Parent) loader.load(getClass().getResource("../Resp.fxml").openStream());
        } catch (IOException e) {
        }
        ResponseController rc = (ResponseController) loader.<ResponseController>getController();
        rc.setMessage(text, 0);

        return parent;
    }

    public void inspectThread(Text header){   //Root message number to get data from database
        GridPane gpane = new GridPane();
        //ListView pane = new ListView();
        FlowPane pane = new FlowPane();
        //pane.setGridLinesVisible(true);
        threadCount=0;
        //pane.add(createField(header.getText(),true,getColorForId()),0,threadCount);
        //pane.add(createResponse("RESP1", "USER1"),0,threadCount);     //Add all responses from database
        //pane.add(createResponse("RESP2", "USER1"),0,threadCount);
        //pane.add(createResponse("RESP3", "USER1"),0,threadCount);

        pane.getChildren().add(constructResponse(weirdText));
        pane.getChildren().add(constructResponse(weirdText));
        ScrollPane sp = new ScrollPane();
        sp.setContent(pane);
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        gpane.add(sp, 0, 1);
        //pane.add(constructResponse(weirdText),0,threadCount++);
        //pane.add(constructResponse(weirdText),0,threadCount++);

        pane.setVgap(20);
        pane.setHgap(20);
        pane.setPadding(new Insets(20, 0, 0, 10));
        pane.setId("content");
        content=gpane;
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

