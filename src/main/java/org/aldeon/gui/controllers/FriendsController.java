package org.aldeon.gui.controllers;

import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.aldeon.gui.GUIController;

import java.net.URL;
import java.util.ResourceBundle;

public class FriendsController  extends BorderPane implements Initializable {
    public VBox sidebar;
    public StackPane logo;
    public GridPane content;
    public StackPane Identities;
    public StackPane Threads;
    public StackPane Friends;
    public StackPane Settings;
    private GUIController root;
    private int idCount;

    public void changeMode(MouseEvent event) throws Exception{
        root.changeMode(event);
    }

    public void setRoot(GUIController root){
        this.root=root;
    }

    public void showDetails(Text id, Text hsh){             //Get from database all the info about id
        final Stage dialogStage = new Stage();
        Text name=id, hash=hsh, additionalInfo=new Text("Reptilian");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(VBoxBuilder.create().
                children(name, hash, additionalInfo).
                alignment(Pos.CENTER).padding(new Insets(5,5,5,5)).spacing(10).build(),250,250);
        scene.setFill(Color.web("#222222"));
        scene.getStylesheets().add("org/aldeon/gui/style.css");
        dialogStage.setScene(scene);
        dialogStage.setTitle("Friend information");
        dialogStage.show();
    }

    public void createTile(String id, String hash, Color clr){
        StackPane newId= new StackPane();
        Rectangle field = new Rectangle(90,135);
        field.setArcHeight(10);
        field.setArcWidth(10);
        field.setFill(clr);
        newId.setPrefSize(90,135);
        Rectangle top = new Rectangle(90,110);
        top.setFill(Color.web("#222222"));
        final Text ident = new Text(id);
        final Text hsh = new Text(hash);
        hsh.setFont(new Font("Verdana", 7));
        newId.setAlignment(Pos.CENTER);
        newId.getChildren().add(field);
        newId.getChildren().add(top);
        newId.setAlignment(ident,Pos.TOP_CENTER);
        StackPane.setMargin(ident,new Insets(20,0,0,0));
        newId.getChildren().add(ident);
        //TODO: ikonka interpolowana z kwadratem spod spodu
        newId.setAlignment(hsh,Pos.BOTTOM_CENTER);
        StackPane.setMargin(hsh, new Insets(0, 0, 20, 0));
        newId.getChildren().add(hsh);
        newId.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                showDetails(new Text(ident.getText()), new Text(hsh.getText()));
            }
        });
        content.add(newId,idCount%6,(int)idCount/6);         //TODO: qunatity depending on window size
        idCount++;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idCount=0;
        Friends.setStyle("-fx-background-color:linear-gradient(from 0% 0% to 100% 0%, #333333, #333333 90%, #1b1b1b 100%);");
        createTile("FRIEND 1", "#HASH1",Color.web("#ffffff"));      //Load all known friends/peers/whatever from DB
        createTile("FRIEND 2", "#HASH2",Color.web("#eeeeee"));
        createTile("FRIEND 3", "#HASH3",Color.web("#dddddd"));
        createTile("FRIEND 4", "#HASH4",Color.web("#cccccc"));
        createTile("FRIEND 5", "#HASH5",Color.web("#bbbbbb"));
    }

}


