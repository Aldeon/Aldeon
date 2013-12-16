package org.aldeon.gui.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.StackPane;
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
import org.aldeon.events.Callback;
import org.aldeon.gui.GUIController;
import org.aldeon.gui.colors.ColorManager;
import org.aldeon.model.Identity;
import org.aldeon.model.Message;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class IdentityManagerController implements Initializable {
    public FlowPane fpane;
    public StackPane Identities;
    public StackPane Threads;
    public StackPane Friends;
    public StackPane Settings;
    public StackPane idCreator;
    public BorderPane main;
    private Map<String,Identity> identities;
    private Map<Key,Parent> guiIds= new HashMap<>();

    public void createId(MouseEvent event) throws Exception{
        final Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        final TextField name = new TextField();
        Button createId = new Button("Create");
        createId.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                dialogStage.close();
                if(name.getText()!=null){
                    Identity newId = Identity.create(name.getText(), new RsaKeyGen());
                    CoreModule.getInstance().getUserManager().addIdentity(newId);
                    identities=CoreModule.getInstance().getUserManager().getAllIdentities();
                    fpane.getChildren().add(constructIdentity(newId));
                }
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

    public void showIdentities(){
        identities=CoreModule.getInstance().getUserManager().getAllIdentities();
        for(Identity id : identities.values()){
            Parent childNode =constructIdentity(id);
            fpane.getChildren().add(childNode);
            guiIds.put(id.getPublicKey(),childNode);
        }
    }

    private Parent constructIdentity(Identity id) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/gui/fxml/Identity.fxml"));
        Parent parent=null;
        try {
            parent = (Parent) loader.load(getClass().getResource("/gui/fxml/Identity.fxml").openStream());
        } catch (IOException e){}
        IdentityController ic = loader.getController();
        final IdentityController icF = ic;
        ic.setId(id,this);
        guiIds.put(id.getPublicKey(),parent);
        return parent;
    }

    public void removeIdentity(Identity id){
        CoreModule.getInstance().getUserManager().delIdentity(id);
        fpane.getChildren().remove(guiIds.get(id.getPublicKey()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        identities=CoreModule.getInstance().getUserManager().getAllIdentities();
        showIdentities();

    }
}
