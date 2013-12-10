package org.aldeon.gui.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.aldeon.core.CoreModule;
import org.aldeon.crypt.rsa.RsaKeyGen;
import org.aldeon.gui.GUIController;
import org.aldeon.gui.colors.ColorManager;
import org.aldeon.model.Identity;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;


public class IdentityController implements Initializable {
    public VBox sidebar;
    public StackPane logo;
    public GridPane content;
    public StackPane Identities;
    public StackPane Threads;
    public StackPane Friends;
    public StackPane Settings;
    public StackPane idCreator;
    public BorderPane main;
    private GUIController root;
    private int idCount;
    private Set<Identity> identities;

    public void setRoot(GUIController root){
        this.root=root;
    }

    public void createId(MouseEvent event) throws Exception{
        final Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        final TextField name = new TextField();
        Button createId = new Button("Create");
        createId.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                dialogStage.close();
                Identity newId = Identity.create(name.getText(), new RsaKeyGen());
                CoreModule.getInstance().addIdentity(newId);
                identities=CoreModule.getInstance().getAllIdentities();
                createTile(name.getText(), newId.getPublicKey().hashCode(), ColorManager.getColorForKey(newId.getPublicKey()));
            }
        });
        Scene scene = new Scene(HBoxBuilder.create().
                children(name, createId).
                alignment(Pos.CENTER).padding(new Insets(5,5,5,5)).spacing(10).build(),250,50);
        scene.setFill(Color.web("#222222"));
        dialogStage.setScene(scene);
        dialogStage.setTitle("Create new ID");
        dialogStage.show();
    }

    public void showDetails(Text id, Text hsh){             //Get from database all the info about id
        final Stage dialogStage = new Stage();
        Text name=id, hash=hsh, additionalInfo=new Text("Reptilian");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(VBoxBuilder.create().
                children(name, hash, additionalInfo).
                alignment(Pos.CENTER).padding(new Insets(5,5,5,5)).spacing(10).build(),250,250);
        scene.setFill(Color.web("#222222"));
        scene.getStylesheets().add("gui/css/style.css");
        dialogStage.setScene(scene);
        dialogStage.setTitle("Friend information");
        dialogStage.show();
    }

    public void createTile(String id, int hash, Color clr){
        StackPane newId= new StackPane();
        Rectangle field = new Rectangle(90,135);
        field.setArcHeight(10);
        field.setArcWidth(10);
        field.setFill(clr);
        newId.setPrefSize(90,135);
        Rectangle top = new Rectangle(90,110);
        top.setFill(Color.web("#222222"));
        final Text ident = new Text(id);
        final Text hsh = new Text(Integer.toString(hash));
        hsh.setFont(new Font("Verdana", 7));
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
        newId.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                showDetails(new Text(ident.getText()), new Text(hsh.getText()));
            }
        });
        idCount++;
        content.add(newId,idCount%6,(int)idCount/6);
    }

    public void showIdentities(){
        for(Identity id : identities){
            createTile(id.getName(), id.getPublicKey().hashCode(), ColorManager.getColorForKey(id.getPublicKey()));
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // Identities.setStyle("-fx-background-color:linear-gradient(from 0% 0% to 100% 0%, #333333, #333333 90%, #1b1b1b 100%);");
        idCount=0;  //TODO: Load all IDs from database/file/whatever
        identities=CoreModule.getInstance().getAllIdentities();

    }

}


