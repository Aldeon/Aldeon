package org.aldeon.gui.listeners;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class IdCreator implements EventHandler {
    private StackPane mainId;
    private int idCount;
    public IdCreator(StackPane parent){
        mainId =parent;
        idCount =0;
    }

    public StackPane newId(String id, String hash, Paint clr){
        StackPane newId= new StackPane();
        Rectangle field = new Rectangle(130,160);
        field.setArcHeight(10);
        field.setArcWidth(10);
        field.setFill(clr);
        newId.setPrefSize(110,140);
        Rectangle top = new Rectangle(130,130);
        top.setFill(Color.web("#222222"));
        Text ident = new Text(id);
        ident.setFont(new Font("Verdana", 12));
        ident.setFill(Color.web("#ffffff"));
        Text hsh = new Text(hash);
        hsh.setFont(new Font("Verdana", 7));
        hsh.setFill(Color.web("#ffffff"));
        newId.setAlignment(Pos.CENTER);
        newId.getChildren().add(field);
        newId.getChildren().add(top);
        newId.setAlignment(ident,Pos.TOP_CENTER);
        StackPane.setMargin(ident,new Insets(20,0,0,0));
        newId.getChildren().add(ident);
        //TODO: ikonka interpolowana z kwadratem spod spodu
        newId.setAlignment(hsh,Pos.BOTTOM_CENTER);
        StackPane.setMargin(hsh,new Insets(0,0,20,0));
        newId.getChildren().add(hsh);
        idCount++;
        return newId;
    }


    @Override
    public void handle(Event event) {
        //StackPane sth= newId(((TextField) mainId.getChildren().get(0)).getText(), Color.web("#ffffff"));          //Color based on a fair dice roll
        StackPane sth= newId("Here be","#Dragons", Color.web("#ffffff"));          //Color based on a fair dice roll
        int colCount = (int)idCount/4;
        ((GridPane) mainId.getParent()).add(sth, idCount % 4, colCount);
    }
}
