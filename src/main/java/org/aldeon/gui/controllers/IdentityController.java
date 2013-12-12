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
import javafx.scene.layout.Pane;
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
import org.aldeon.crypt.Key;
import org.aldeon.crypt.rsa.RsaKeyGen;
import org.aldeon.gui.GUIController;
import org.aldeon.gui.colors.ColorManager;
import org.aldeon.model.Identity;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;


public class IdentityController implements Initializable {


    public Pane idPane;
    public Rectangle colorRectangle;
    public Rectangle backgroundRectangle;
    public Text authorName;
    public Text authorPubKey;

    private Identity myId;
    private IdentityManagerController parentController;

    public void setId(Identity id, IdentityManagerController parent){
        this.myId=id;
        this.parentController=parent;
        authorName.setText(id.getName());
        authorPubKey.setText(id.getPublicKey().toString().substring(0, 8) + "...");
        colorRectangle.setFill(ColorManager.getColorForKey(id.getPublicKey()));
    }

    public void showDetails(){             //Get from database all the info about id
        final Stage dialogStage = new Stage();
        Text name=new Text(myId.getName()), hash=new Text(myId.getPublicKey().toString().substring(0,8)+"..."), additionalInfo=new Text("Additional information");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Button delete = new Button("Delete");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                dialogStage.close();
                parentController.removeIdentity(myId);
            }
        });
        Scene scene = new Scene(VBoxBuilder.create().
                children(name, hash, additionalInfo,delete).
                alignment(Pos.CENTER).padding(new Insets(5,5,5,5)).spacing(10).build(),250,250);
        scene.setFill(Color.web("#222222"));
        scene.getStylesheets().add("gui/css/style.css");
        dialogStage.setScene(scene);
        dialogStage.setTitle("Friend information");
        dialogStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
      
    
}


