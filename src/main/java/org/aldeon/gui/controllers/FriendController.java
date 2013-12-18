package org.aldeon.gui.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.aldeon.gui.colors.ColorManager;
import org.aldeon.model.Identity;
import org.aldeon.model.User;
import org.aldeon.model.UserImpl;

import java.net.URL;
import java.util.ResourceBundle;

public class FriendController implements Initializable {
    public Pane idPane;
    public Rectangle colorRectangle;
    public Rectangle backgroundRectangle;
    public Text authorName;
    public Text authorPubKey;

    private User myId;
    private FriendsManagerController parentController;

    public void setUser(User id, FriendsManagerController parent){
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
        final TextField usrName = new TextField();
        Button rename = new Button("Rename user");
        rename.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                dialogStage.close();
                parentController.renameUser(UserImpl.create(usrName.getText(), myId.getPublicKey()));
            }
        });
        HBox renameBox = new HBox();
        renameBox.getChildren().add(usrName);
        renameBox.getChildren().add(rename);
        renameBox.setSpacing(10.0);
        Scene scene = new Scene(VBoxBuilder.create().
                children(name, hash, additionalInfo,renameBox).
                alignment(Pos.CENTER).padding(new Insets(5,5,5,5)).spacing(10).build(),280,150);
        scene.setFill(Color.web("#444444"));
        scene.getStylesheets().add("gui/css/style.css");
        dialogStage.setScene(scene);
        dialogStage.setTitle("Friend information");
        dialogStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

}


